package us.timinc.mc.timcore.api.event

/**
 * A subscribe-only event surface for code that should be able to listen, but not fire the event.
 */
interface Subscribable<In, Out> {
    fun subscribe(subscription: Subscription<In, Out>): Subscription<In, Out>
    fun unsubscribe(subscription: Subscription<In, Out>)

    /**
     * Returns a wrapper that forwards subscription calls to this instance without exposing any wider access.
     */
    fun asSubscribable(): Subscribable<In, Out> =
        object : Subscribable<In, Out> {
            override fun subscribe(subscription: Subscription<In, Out>): Subscription<In, Out> =
                this@Subscribable.subscribe(subscription)

            override fun unsubscribe(subscription: Subscription<In, Out>) {
                this@Subscribable.unsubscribe(subscription)
            }
        }
}