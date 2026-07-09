package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class SubscriptionTest {
    private fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("test", path)

    @Test
    fun `keeps its id and listener`() {
        val listener: (Unit) -> Unit = {}
        val subscriptionId = id("subscription")

        val subscription = Subscription(subscriptionId, listener)

        assertSame(subscriptionId, subscription.id)
        assertSame(listener, subscription.listener)
    }
}
