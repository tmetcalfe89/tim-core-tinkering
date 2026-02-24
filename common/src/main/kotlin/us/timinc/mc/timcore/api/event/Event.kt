package us.timinc.mc.timcore.api.event

/**
 * An event. You can subscribe to it, adding a callback to its list of listeners, with an optional priority. When it
 * fires, all subscribers will be called with the data for the event.
 *
 * @author Timothy Metcalfe
 */
open class Event<T> {
    private val subscribers = Array(Priority.entries.size) { LinkedHashSet<Subscription<T>>() }

    /**
     * Adds a new subscription and returns it.
     */
    open fun subscribe(subscription: Subscription<T>): Subscription<T> {
        subscribers[subscription.priority.ordinal].add(subscription)
        return subscription
    }

    /**
     * Creates a new subscription with the given listener and priority, adds it, and returns it.
     */
    @Suppress("unused")
    fun subscribe(listener: (T) -> Unit, priority: Priority = Priority.NORMAL) =
        subscribe(Subscription(listener, priority))

    /**
     * Creates a new subscription with the given listener and normal priority, adds it, and returns it.
     */
    fun subscribe(listener: (T) -> Unit): Subscription<T> =
        subscribe(listener)

    /**
     * Removes an existing subscription.
     */
    @Suppress("unused")
    open fun unsubscribe(subscription: Subscription<T>) {
        subscribers[subscription.priority.ordinal].remove(subscription)
    }

    /**
     * Fires an event, calling every subscribed listener with the given data.
     *
     * @sample us.timinc.mc.timcore.TimCore.timCoreSpecificInit
     */
    open fun fire(data: T) {
        for (prioritySubscriptions in subscribers) {
            for (subscription in prioritySubscriptions) {
                subscription.listener(data)
            }
        }
    }
}