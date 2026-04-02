package us.timinc.mc.timcore.api.cobblemon

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeatures
import com.cobblemon.mod.common.api.properties.CustomPokemonProperty
import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.event.OneTimeEvent
import us.timinc.mc.timcore.api.mod.PlatformBits
import us.timinc.mc.timcore.api.module.AbstractModule

/**
 * The Cobblemon Tim Core module, offering a convenience layer for various registrations between your mod and Cobblemon.
 *
 * @author Timothy Metcalfe
 */
object CobblemonModule : AbstractModule<TimCore>(TimCore, "cobblemon") {
    object Events {
        @JvmField
        val COBBLEMON_INITIALIZED = OneTimeEvent<Unit>()
    }

    private val customPokemonProperties: MutableList<CustomPokemonPropertyType<*>> = mutableListOf()

    /**
     * Register a new custom Pokemon property. Registers it with Cobblemon such that it shows up in autocomplete for
     * commands such as `pokeedit`, `pokespawn`, and `pokegive` (anything that uses PokemonProperties).
     *
     * @author Timothy Metcalfe
     */
    fun <PT : CustomPokemonProperty, T : CustomPokemonPropertyType<PT>> registerCustomPokemonProperty(prop: T): T {
        logger.sing("Registering custom pokemon property internally for later registration with Cobblemon: ${prop.keys.first()}")
        customPokemonProperties.add(prop)
        return prop
    }

    override fun init(platformBits: PlatformBits) {
        logger.sing("Listening for Cobblemon to load to do setup.")
        Events.COBBLEMON_INITIALIZED.subscribe {
            logger.sing("Registering ${customPokemonProperties.size} custom pokemon properties: ${customPokemonProperties.map { it.keys.first() }}")
            for (prop in customPokemonProperties) {
                CustomPokemonProperty.register(prop)
            }
        }

        // Jank solution to shoehorn the SimpleObservable that is COBBLEMON_INITIALIZED into a FireAndForgetObservable.
        // I'm sure it won't backfire by having a sidemod register a species feature before Cobblemon gets the chance to
        // register its.
        // TODO: Find a solution to avoid the above issue.
        when (SpeciesFeatures.types.isEmpty()) {
            true -> {
                logger.sing("Cobblemon not yet initialized. Listening for Cobblemon to load.")
                CobblemonEvents.COBBLEMON_INITIALISED.subscribe {
                    logger.sing("Cobblemon initialized. Firing Tim Core Cobblemon load event.")
                    Events.COBBLEMON_INITIALIZED.fire(Unit)
                }
            }

            false -> {
                logger.sing("Cobblemon already initialized. Firing Tim Core Cobblemon load event.")
                Events.COBBLEMON_INITIALIZED.fire(Unit)
            }
        }
    }
}