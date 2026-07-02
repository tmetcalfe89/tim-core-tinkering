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
    val MODULE_LOAD: Subscribable<PlatformBits> = moduleLoad.asSubscribable()

    /**
     * Fired by Tim Core during the setup phase to awaken any used features. Features automatically listen for this on
     * instantiation, so there's no need to reference it manually.
     */
    @JvmField
    val FEATURE_LOAD: Subscribable<Unit> = featureLoad.asSubscribable()

    internal fun fireModuleLoad(platformBits: PlatformBits) {
        moduleLoad.fire(platformBits)
    }

    internal fun fireFeatureLoad() {
        featureLoad.fire(Unit)
    }

    private fun <T> Event<T>.asSubscribable(): Subscribable<T> =
        object : Subscribable<T> {
            override fun subscribe(subscription: Subscription<T>): Subscription<T> =
                this@asSubscribable.subscribe(subscription)

            override fun subscribeWithPriority(listener: (T) -> Unit, priority: Priority): Subscription<T> =
                this@asSubscribable.subscribeWithPriority(listener, priority)

            override fun subscribe(listener: (T) -> Unit): Subscription<T> =
                this@asSubscribable.subscribe(listener)

            override fun unsubscribe(subscription: Subscription<T>) {
                this@asSubscribable.unsubscribe(subscription)
            }
        }
}
