package us.timinc.mc.timcore.api.event

import us.timinc.mc.timcore.api.mod.PlatformBits

object TimCoreEvents {
    private val moduleLoad = OneTimeEvent<PlatformBits>()
    private val featureLoad = OneTimeEvent<Unit>()

    /**
     * Fired by Tim Core during the setup phase to awaken any used modules. Modules automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val MODULE_LOAD: Subscribable<PlatformBits> = moduleLoad

    /**
     * Fired by Tim Core during the setup phase to awaken any used features. Features automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val FEATURE_LOAD: Subscribable<Unit> = featureLoad

    internal fun fireModuleLoad(platformBits: PlatformBits) {
        moduleLoad.fire(platformBits)
    }

    internal fun fireFeatureLoad() {
        featureLoad.fire(Unit)
    }
}
