package us.timinc.mc.timcore.api.event

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class SubscriptionTest {
    @Test
    fun `defaults to normal priority`() {
        val listener: (Unit) -> Unit = {}

        val subscription = Subscription(listener)

        assertSame(listener, subscription.listener)
        assertEquals(Priority.NORMAL, subscription.priority)
    }

    @Test
    fun `keeps explicit priority`() {
        val listener: (Unit) -> Unit = {}

        val subscription = Subscription(listener, Priority.HIGH)

        assertSame(listener, subscription.listener)
        assertEquals(Priority.HIGH, subscription.priority)
    }
}
