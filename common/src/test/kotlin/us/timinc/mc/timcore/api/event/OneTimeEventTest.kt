package us.timinc.mc.timcore.api.event

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class OneTimeEventTest {
    @Test
    fun `fires existing subscribers once`() {
        val event = OneTimeEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe { calls.add(it) }
        event.fire("ready")

        assertEquals(listOf("ready"), calls)
    }

    @Test
    fun `late subscribers immediately receive the completed value`() {
        val event = OneTimeEvent<String>()
        val calls = mutableListOf<String>()

        event.fire("ready")
        event.subscribe { calls.add(it) }

        assertEquals(listOf("ready"), calls)
    }

    @Test
    fun `does not retain subscribers after completion`() {
        val event = OneTimeEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe { calls.add("early:$it") }
        event.fire("ready")
        event.subscribe { calls.add("late:$it") }

        assertEquals(listOf("early:ready", "late:ready"), calls)
    }

    @Test
    fun `throws when fired more than once`() {
        val event = OneTimeEvent<String>()

        event.fire("ready")

        assertThrows(IllegalStateException::class.java) {
            event.fire("again")
        }
    }
}
