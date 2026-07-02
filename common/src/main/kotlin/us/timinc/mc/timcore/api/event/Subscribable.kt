package us.timinc.mc.timcore.api.event

/**
 * A subscribe-only event surface for code that should be able to listen, but not fire the event.
 */
interface Subscribable<T> {
    fun subscribe(subscription: Subscription<T>): Subscription<T>
    fun subscribeWithPriority(listener: (T) -> Unit, priority: Priority = Priority.NORMAL): Subscription<T>
    fun subscribe(listener: (T) -> Unit): Subscription<T>
    fun unsubscribe(subscription: Subscription<T>)
}