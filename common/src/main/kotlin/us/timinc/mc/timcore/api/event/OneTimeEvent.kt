package us.timinc.mc.timcore.api.event

/**
 * An event that's only fired once. After firing, it will immediately fire the event on incoming subscriptions. Useful
 * for things such as one-time initial load events, so new subscriptions that subscribe *after* the event is fired don't
 * get caught in a race condition where it's too late for them.
 *
 * @author Timothy Metcalfe
 */
class OneTimeEvent<T : Any> : Event<T>() {
    private var completedValue: T? = null

    /**
     * Register a new subscription. If this event has already fired, call the subscription and do not register it.
     */
    override fun subscribe(subscription: Subscription<T>): Subscription<T> {
        completedValue?.let {
            subscription.listener(it)

            return subscription
        }
        return super.subscribe(subscription)
    }

    /**
     * Fires the event, remembering the event value, and clearing all subscriptions afterward.
     *
     * @throws [Exception] If the event was already fired.
     */
    override fun fire(data: T) {
        completedValue?.let {
            throw Exception("This one-time event already completed.")
        }
        completedValue = data
        super.fire(data)
        for (prioritySubs in subscribers) {
            prioritySubs.clear()
        }
    }
}