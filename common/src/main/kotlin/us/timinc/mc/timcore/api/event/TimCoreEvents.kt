package us.timinc.mc.timcore.api.event

object TimCoreEvents {
    /**
     * DO NOT FIRE THIS
     *
     * Fired by Tim Core during the setup phase to awaken any used modules. Modules automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val MODULE_LOAD = OneTimeEvent<Unit>()

    /**
     * DO NOT FIRE THIS
     *
     * Fired by Tim Core during the setup phase to awaken any used features. Features automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val FEATURE_LOAD = OneTimeEvent<Unit>()
}