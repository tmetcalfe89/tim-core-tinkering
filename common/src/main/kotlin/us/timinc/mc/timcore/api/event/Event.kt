package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation

/**
 * An event. You can subscribe to it, adding a callback to its list of listeners. When it fires, all subscribers will be
 * called with the data for the event. No listener ordering is guaranteed. Subscriptions are expected to be registered
 * before the event is actively firing.
 *
 * @author Timothy Metcalfe
 */
open class Event<In, Out> : Subscribable<In, Out> {
    protected val subscribers: MutableMap<ResourceLocation, Subscription<In, Out>> = hashMapOf()

    /**
     * Adds a new subscription and returns it.
     */
    override fun subscribe(subscription: Subscription<In, Out>): Subscription<In, Out> {
        if (subscribers.containsKey(subscription.id)) throw IllegalArgumentException("Duplicate subscriber ID ${subscription.id}")
        subscribers[subscription.id] = subscription
        return subscription
    }

    /**
     * Removes an existing subscription.
     */
    @Suppress("unused")
    override fun unsubscribe(subscription: Subscription<In, Out>) {
        subscribers.remove(subscription.id)
    }

    /**
     * Fires an event, calling every subscribed listener with the given data.
     */
    open fun fire(data: In) {
        subscribers.values.forEach { it.listener(data) }
    }
}
