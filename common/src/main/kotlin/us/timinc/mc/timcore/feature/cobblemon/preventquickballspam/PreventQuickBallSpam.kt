package us.timinc.mc.timcore.feature.cobblemon.preventquickballspam

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.cobblemon.CobblemonModule
import us.timinc.mc.timcore.api.cobblemon.property.PersistentDataProperty
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.handler.QuickBallSpamCatchRate

object PreventQuickBallSpam : AbstractFeature<TimCore, PreventQuickBallSpam.Config>(
    TimCore,
    "prevent_quick_ball_spam",
    Config::class,
    setOf("cobblemon"),
) {
    class Config : FeatureConfig()

    /**
     * @see PersistentDataProperty
     */
    object PokemonProperties {
        /**
         * @see PersistentDataProperty.Boolean
         */
        val immuneToQuickBall =
            CobblemonModule.registerCustomPokemonProperty(PersistentDataProperty.Boolean(TimCore, "immune_to_quick_ball"))
    }

    override fun initialize() {
        PokemonProperties

        CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.LOWEST, QuickBallSpamCatchRate::handle)
    }
}