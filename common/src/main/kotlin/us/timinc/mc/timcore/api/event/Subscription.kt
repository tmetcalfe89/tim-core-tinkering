package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation

/**
 * A simple encapsulation of an event subscription.
 */
class Subscription<T>(
    val id: ResourceLocation,
    val listener: (T) -> Unit,
)