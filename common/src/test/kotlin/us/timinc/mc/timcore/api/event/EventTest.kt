package us.timinc.mc.timcore.api.event

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class EventTest {
    @Test
    fun `fires subscribers from highest priority to lowest priority`() {
        val event = Event<Unit>()
        val calls = mutableListOf<Priority>()

        event.subscribeWithPriority({ calls.add(Priority.LOWEST) }, Priority.LOWEST)
        event.subscribeWithPriority({ calls.add(Priority.LOW) }, Priority.LOW)
        event.subscribeWithPriority({ calls.add(Priority.NORMAL) }, Priority.NORMAL)
        event.subscribeWithPriority({ calls.add(Priority.HIGH) }, Priority.HIGH)
        event.subscribeWithPriority({ calls.add(Priority.HIGHEST) }, Priority.HIGHEST)

        event.fire(Unit)

        assertEquals(
            listOf(Priority.HIGHEST, Priority.HIGH, Priority.NORMAL, Priority.LOW, Priority.LOWEST),
            calls
        )
    }

    @Test
    fun `subscribe returns the created subscription with normal priority`() {
        val event = Event<Unit>()
        val listener: (Unit) -> Unit = {}

        val subscription = event.subscribe(listener)

        assertSame(listener, subscription.listener)
        assertEquals(Priority.NORMAL, subscription.priority)
    }

    @Test
    fun `unsubscribe prevents future dispatch`() {
        val event = Event<Unit>()
        val calls = mutableListOf<String>()

        val subscription = event.subscribe {
            calls.add("called")
        }
        event.unsubscribe(subscription)
        event.fire(Unit)

        assertEquals(emptyList<String>(), calls)
    }

    @Test
    fun `unsubscribe during fire does not invalidate dispatch`() {
        val event = Event<Unit>()
        val calls = mutableListOf<String>()

        lateinit var second: Subscription<Unit>
        event.subscribe {
            calls.add("first")
            event.unsubscribe(second)
        }
        second = event.subscribe {
            calls.add("second")
        }

        event.fire(Unit)

        assertEquals(listOf("first"), calls)
    }

    @Test
    fun `subscribe during fire waits until the next fire`() {
        val event = Event<Unit>()
        val calls = mutableListOf<String>()

        event.subscribe {
            calls.add("first")
            event.subscribe {
                calls.add("second")
            }
        }

        event.fire(Unit)
        assertEquals(listOf("first"), calls)

        event.fire(Unit)
        assertEquals(listOf("first", "first", "second"), calls)
    }
}
