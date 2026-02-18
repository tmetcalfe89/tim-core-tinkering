package us.timinc.mc.timcore.feature.preventquickballspam

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.cobblemon.property.PersistentDataProperty
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.feature.preventquickballspam.handler.QuickBallSpamCatchRate

object PreventQuickBallSpam : AbstractFeature<TimCore, FeatureConfig>(
    TimCore,
    "prevent_quick_ball_spam",
    FeatureConfig::class,
    listOf("cobblemon")
) {
    object PokemonProperties {
        val immuneToQuickBall = PersistentDataProperty.Boolean(TimCore, "immune_to_quick_ball")
    }

    override fun initialize() {
        CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.LOWEST, withFeature(QuickBallSpamCatchRate::handle))
    }
}