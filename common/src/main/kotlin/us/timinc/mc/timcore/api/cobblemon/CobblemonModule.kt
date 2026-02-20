package us.timinc.mc.timcore.api.cobblemon

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeatures
import com.cobblemon.mod.common.api.properties.CustomPokemonProperty
import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.event.Priority
import us.timinc.mc.timcore.api.module.AbstractModule

object CobblemonModule : AbstractModule<TimCore>(TimCore, "cobblemon") {
    val customPokemonProperties: MutableList<CustomPokemonPropertyType<*>> = mutableListOf()

    fun <PT : CustomPokemonProperty, T : CustomPokemonPropertyType<PT>> registerCustomPokemonProperty(prop: T): T {
        logger.sing("Registering custom pokemon property internally for later registration with Cobblemon: ${prop.keys.first()}")
        customPokemonProperties.add(prop)
        return prop
    }

    override fun init() {
        logger.sing("Listening for Cobblemon to load to do setup.")
        TimCobblemonEvents.COBBLEMON_INITIALIZED.subscribe(Priority.NORMAL) {
            logger.sing("Registering ${customPokemonProperties.size} custom pokemon properties: ${customPokemonProperties.map { it.keys.first() }}")
            for (prop in customPokemonProperties) {
                CustomPokemonProperty.register(prop)
            }
        }

        // Jank solution to shoehorn the SimpleObservable that is COBBLEMON_INITIALIZED into a FireAndForgetObservable.
        when (SpeciesFeatures.types.isEmpty()) {
            true -> {
                logger.sing("Cobblemon not yet initialized. Listening for Cobblemon to load.")
                CobblemonEvents.COBBLEMON_INITIALISED.subscribe {
                    logger.sing("Cobblemon initialized. Firing Tim Core Cobblemon load event.")
                    TimCobblemonEvents.COBBLEMON_INITIALIZED.fire(Unit)
                }
            }

            false -> {
                logger.sing("Cobblemon already initialized. Firing Tim Core Cobblemon load event.")
                TimCobblemonEvents.COBBLEMON_INITIALIZED.fire(Unit)
            }
        }
    }
}