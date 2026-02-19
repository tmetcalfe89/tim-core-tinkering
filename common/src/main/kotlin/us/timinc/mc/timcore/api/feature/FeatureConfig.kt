package us.timinc.mc.timcore.api.feature

import us.timinc.mc.timcore.api.logging.Logger

open class FeatureConfig {
    val enabled: Boolean = true
    val debugLevel: Logger.LogLevel = Logger.LogLevel.WARN
}