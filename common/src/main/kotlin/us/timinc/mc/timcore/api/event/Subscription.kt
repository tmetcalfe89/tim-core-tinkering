package us.timinc.mc.timcore.api.event

class Subscription<T>(
    val listener: (T) -> Unit,
    val priority: Priority = Priority.NORMAL,
)