package us.timinc.mc.timcore.api.event

object TimCoreEvents {
    @JvmField
    val MODULE_LOAD = OneTimeEvent<Unit>()

    @JvmField
    val FEATURE_LOAD = OneTimeEvent<Unit>()
}