package us.timinc.mc.timcore.api.mod

import us.timinc.mc.timcore.api.logging.Logger

abstract class ModConfig {
    val debugLevel: Logger.LogLevel = Logger.LogLevel.WARN
}