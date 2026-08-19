package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation

/**
 * A simple encapsulation of an event subscription.
 */
class Subscription<In, Out>(
    val id: ResourceLocation,
    val listener: (In) -> Out,
)