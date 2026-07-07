package us.timinc.mc.timcore.api.event

/**
 * A subscribe-only event surface for code that should be able to listen, but not fire the event.
 */
interface Subscribable<T> {
    fun subscribe(subscription: Subscription<T>): Subscription<T>
    fun subscribeWithPriority(listener: (T) -> Unit, priority: Priority = Priority.NORMAL): Subscription<T>
    fun subscribe(listener: (T) -> Unit): Subscription<T>
    fun unsubscribe(subscription: Subscription<T>)

    /**
     * Returns a wrapper that forwards subscription calls to this instance without exposing any wider access.
     */
    fun asSubscribable(): Subscribable<T> =
        object : Subscribable<T> {
            override fun subscribe(subscription: Subscription<T>): Subscription<T> =
                this@Subscribable.subscribe(subscription)

            override fun subscribeWithPriority(listener: (T) -> Unit, priority: Priority): Subscription<T> =
                this@Subscribable.subscribeWithPriority(listener, priority)

            override fun subscribe(listener: (T) -> Unit): Subscription<T> =
                this@Subscribable.subscribe(listener)

            override fun unsubscribe(subscription: Subscription<T>) {
                this@Subscribable.unsubscribe(subscription)
            }
        }
}