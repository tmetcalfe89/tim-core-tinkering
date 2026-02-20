package us.timinc.mc.timcore.api.cobblemon

import us.timinc.mc.timcore.api.event.OneTimeEvent

object TimCobblemonEvents {
    @JvmField
    val COBBLEMON_INITIALIZED = OneTimeEvent<Unit>()
}