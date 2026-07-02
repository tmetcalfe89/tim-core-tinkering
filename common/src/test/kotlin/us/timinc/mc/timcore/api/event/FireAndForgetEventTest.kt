package us.timinc.mc.timcore.api.event

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FireAndForgetEventTest {
    @Test
    fun `late subscribers immediately receive the last fired value and remain subscribed`() {
        val event = FireAndForgetEvent<String>()
        val calls = mutableListOf<String>()

        event.fire("first")
        event.subscribe { calls.add(it) }
        event.fire("second")

        assertEquals(listOf("first", "second"), calls)
    }

    @Test
    fun `can fire repeatedly`() {
        val event = FireAndForgetEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe { calls.add(it) }
        event.fire("first")
        event.fire("second")

        assertEquals(listOf("first", "second"), calls)
    }

    @Test
    fun `reset clears replayed value without removing subscribers`() {
        val event = FireAndForgetEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe { calls.add("early:$it") }
        event.fire("first")
        event.reset()
        event.subscribe { calls.add("late:$it") }
        event.fire("second")

        assertEquals(listOf("early:first", "early:second", "late:second"), calls)
    }
}
