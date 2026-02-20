package us.timinc.mc.timcore.api.event

class Subscription<T>(
    val priority: Priority,
    val listener: (T) -> Unit,
)