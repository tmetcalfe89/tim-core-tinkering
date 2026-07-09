package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FireAndForgetEventTest {
    private fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("test", path)

    @Test
    fun `late subscribers immediately receive the last fired value and remain subscribed`() {
        val event = FireAndForgetEvent<String>()
        val calls = mutableListOf<String>()

        event.fire("first")
        event.subscribe(Subscription(id("subscription")) { calls.add(it) })
        event.fire("second")

        assertEquals(listOf("first", "second"), calls)
    }

    @Test
    fun `can fire repeatedly`() {
        val event = FireAndForgetEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe(Subscription(id("subscription")) { calls.add(it) })
        event.fire("first")
        event.fire("second")

        assertEquals(listOf("first", "second"), calls)
    }

    @Test
    fun `reset clears replayed value without removing subscribers`() {
        val event = FireAndForgetEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe(Subscription(id("early")) { calls.add("early:$it") })
        event.fire("first")
        event.reset()
        event.subscribe(Subscription(id("late")) { calls.add("late:$it") })
        event.fire("second")

        assertEquals(listOf("early:first", "early:second", "late:second"), calls.sorted())
    }
}
