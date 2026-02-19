package us.timinc.mc.timcore.api.config

import us.timinc.mc.timcore.api.logging.Logger

abstract class ModuleConfig {
    val debugLevel: Logger.LogLevel = Logger.LogLevel.WARN
}