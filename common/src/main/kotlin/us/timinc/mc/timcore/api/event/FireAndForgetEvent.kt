package us.timinc.mc.timcore.api.event

class FireAndForgetEvent<T> : Event<T>() {
    private var completedValue: T? = null

    override fun subscribe(subscription: Subscription<T>): Subscription<T> {
        completedValue?.let {
            subscription.listener(it)
            return subscription
        }
        return super.subscribe(subscription)
    }

    override fun fire(data: T) {
        completedValue = data
        super.fire(data)
    }

    fun reset() {
        completedValue = null
    }
}