package us.timinc.mc.timcore.api.event

/**
 * An event. You can subscribe to it, adding a callback to its list of listeners, with an optional priority.
 */
open class Event<T> {
    val subscribers = Array(Priority.entries.size) { LinkedHashSet<Subscription<T>>() }

    open fun subscribe(subscription: Subscription<T>): Subscription<T> {
        subscribers[subscription.priority.ordinal].add(subscription)
        return subscription
    }

    fun subscribe(priority: Priority, listener: (T) -> Unit) =
        subscribe(Subscription(priority, listener))

    @Suppress("unused")
    open fun unsubscribe(subscription: Subscription<T>) {
        subscribers[subscription.priority.ordinal].remove(subscription)
    }

    open fun fire(data: T) {
        for (prioritySubscriptions in subscribers) {
            for (subscription in prioritySubscriptions) {
                subscription.listener(data)
            }
        }
    }
}