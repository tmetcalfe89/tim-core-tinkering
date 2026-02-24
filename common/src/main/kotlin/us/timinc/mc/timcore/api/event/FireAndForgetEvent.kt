package us.timinc.mc.timcore.api.event

/**
 * An event that's fired once, and is treated as though it won't be fired again until it's reset. While already fired,
 * it will immediately fire the event on incoming subscriptions. Useful for things like events that are fired on server
 * start, so new subscriptions that subscribe *after* the event is fired don't get caught in a race condition where it's
 * too late for them. When the server's stopped, the event can be reset.
 *
 * @author Timothy Metcalfe
 */
@Suppress("unused")
class FireAndForgetEvent<T> : Event<T>() {
    private var completedValue: T? = null

    /**
     * Register a new subscription. If this event has already fired, call the subscription as well.
     */
    override fun subscribe(subscription: Subscription<T>): Subscription<T> {
        completedValue?.let(subscription.listener)
        return super.subscribe(subscription)
    }

    /**
     * Fire the event. Saves the completed value to autocomplete future subscriptions.
     */
    override fun fire(data: T) {
        completedValue = data
        super.fire(data)
    }

    /**
     * Reset the previously completed event.
     */
    fun reset() {
        completedValue = null
    }
}