package us.timinc.mc.timcore.api.event

/**
 * A simple encapsulation of an event subscription. At the moment, contains both the listener and the priority. See
 * [Priority] for more info about my intent there. Even if priority is removed or altered in the future, this class
 * will remain as an encapsulation class.
 */
class Subscription<T>(
    val listener: (T) -> Unit,
    val priority: Priority = Priority.NORMAL,
)