package us.timinc.mc.timcore.api.event

/**
 * A subscribe-only event surface for code that should be able to listen, but not fire the event.
 */
interface Subscribable<T> {
    fun subscribe(subscription: Subscription<T>): Subscription<T>
    fun unsubscribe(subscription: Subscription<T>)

    /**
     * Returns a wrapper that forwards subscription calls to this instance without exposing any wider access.
     */
    fun asSubscribable(): Subscribable<T> =
        object : Subscribable<T> {
            override fun subscribe(subscription: Subscription<T>): Subscription<T> =
                this@Subscribable.subscribe(subscription)

            override fun unsubscribe(subscription: Subscription<T>) {
                this@Subscribable.unsubscribe(subscription)
            }
        }
}