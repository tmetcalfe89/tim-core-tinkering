package us.timinc.mc.timcore.api.event

import net.minecraft.resources.ResourceLocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class OneTimeEventTest {
    private fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("test", path)

    @Test
    fun `fires existing subscribers once`() {
        val event = OneTimeEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe(Subscription(id("subscription")) { calls.add(it) })
        event.fire("ready")

        assertEquals(listOf("ready"), calls)
    }

    @Test
    fun `late subscribers immediately receive the completed value`() {
        val event = OneTimeEvent<String>()
        val calls = mutableListOf<String>()

        event.fire("ready")
        event.subscribe(Subscription(id("subscription")) { calls.add(it) })

        assertEquals(listOf("ready"), calls)
    }

    @Test
    fun `does not retain subscribers after completion`() {
        val event = OneTimeEvent<String>()
        val calls = mutableListOf<String>()

        event.subscribe(Subscription(id("early")) { calls.add("early:$it") })
        event.fire("ready")
        event.subscribe(Subscription(id("late")) { calls.add("late:$it") })

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
