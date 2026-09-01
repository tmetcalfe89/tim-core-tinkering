package us.timinc.mc.timcore.feature.cobblemon.evgainmultiplier

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.feature.cobblemon.evgainmultiplier.handler.EvGainMultiplierHandler

object EvGainMultiplier : AbstractFeature<TimCore, EvGainMultiplier.Config>(
    TimCore,
    "ev_gain_multiplier",
    Config::class,
    setOf("cobblemon"),
) {
    class Config(
        enabled: Boolean = true,
        debugLevel: Logger.LogLevel = Logger.LogLevel.WARN,
        val multiplier: Double = 1.0,
    ) : FeatureConfig(enabled, debugLevel)

    override fun initialize() {
        CobblemonEvents.EV_GAINED_EVENT_PRE.subscribe(Priority.NORMAL, EvGainMultiplierHandler::handle)
    }
}
