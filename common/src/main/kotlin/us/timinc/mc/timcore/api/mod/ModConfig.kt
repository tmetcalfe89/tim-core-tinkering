package us.timinc.mc.timcore.api.mod

import us.timinc.mc.timcore.api.logging.Logger

/**
 * The basis for a [AbstractMod]'s config. Feel free to override this and use it in your mod to add more to it.
 */
abstract class ModConfig {
    /**
     * What level this mod's logger will log for. Any logs emitted to the logger at or above this priority will show in
     * the user's logs.
     */
    val debugLevel: Logger.LogLevel = Logger.LogLevel.WARN
}