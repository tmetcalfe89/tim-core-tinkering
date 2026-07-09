package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EventTest {
    private fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("test", path)

    @Test
    fun `fires subscribers`() {
        val event = Event<Unit>()
        val calls = mutableListOf<String>()

        event.subscribe(Subscription(id("first")) { calls.add("first") })
        event.subscribe(Subscription(id("second")) { calls.add("second") })
        event.subscribe(Subscription(id("third")) { calls.add("third") })

        event.fire(Unit)

        assertEquals(listOf("first", "second", "third"), calls.sorted())
    }

    @Test
    fun `subscribe returns the provided subscription`() {
        val event = Event<Unit>()
        val listener: (Unit) -> Unit = {}
        val subscription = Subscription(id("subscription"), listener)

        val result = event.subscribe(subscription)

        assertSame(subscription, result)
        assertSame(listener, result.listener)
    }

    @Test
    fun `duplicate subscription ids are rejected`() {
        val event = Event<Unit>()
        val calls = mutableListOf<String>()

        event.subscribe(Subscription(id("same")) { calls.add("first") })
        assertThrows(IllegalArgumentException::class.java) {
            event.subscribe(Subscription(id("same")) { calls.add("second") })
        }
        event.fire(Unit)

        assertEquals(listOf("first"), calls)
    }

    @Test
    fun `unsubscribe prevents future dispatch`() {
        val event = Event<Unit>()
        val calls = mutableListOf<String>()

        val subscription = event.subscribe(Subscription(id("subscription")) {
            calls.add("called")
        })
        event.unsubscribe(subscription)
        event.fire(Unit)

        assertEquals(emptyList<String>(), calls)
    }
}
