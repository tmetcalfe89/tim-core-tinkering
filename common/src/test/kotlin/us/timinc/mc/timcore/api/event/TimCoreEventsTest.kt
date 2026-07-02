package us.timinc.mc.timcore.api.event

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class TimCoreEventsTest {
    @Test
    fun `module load exposes only a subscribable wrapper`() {
        assertFalse(TimCoreEvents.MODULE_LOAD is OneTimeEvent<*>)
    }

    @Test
    fun `feature load exposes only a subscribable wrapper`() {
        assertFalse(TimCoreEvents.FEATURE_LOAD is OneTimeEvent<*>)
    }
}
