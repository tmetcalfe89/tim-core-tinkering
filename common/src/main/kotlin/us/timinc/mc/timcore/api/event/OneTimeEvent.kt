package us.timinc.mc.timcore.api.event

class OneTimeEvent<T> : Event<T>() {
    private var completedValue: T? = null

    override fun subscribe(subscription: Subscription<T>): Subscription<T> {
        completedValue?.let {
            subscription.listener(it)
            return subscription
        }
        return super.subscribe(subscription)
    }

    override fun fire(data: T) {
        completedValue?.let {
            throw Exception("This one-time event already completed.")
        }
        completedValue = data
        super.fire(data)
    }
}