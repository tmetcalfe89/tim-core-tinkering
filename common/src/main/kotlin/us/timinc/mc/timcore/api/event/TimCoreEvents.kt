package us.timinc.mc.timcore.api.event

import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.mod.PlatformBits

object TimCoreEvents {
    /**
     * Fired by Tim Core during the setup phase to awaken any used modules. Modules automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val MODULE_LOAD: Subscribable<PlatformBits> = TimCore.getModuleLoad()

    /**
     * Fired by Tim Core during the setup phase to awaken any used features. Features automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val FEATURE_LOAD: Subscribable<Unit> = TimCore.getFeatureLoad()
}
