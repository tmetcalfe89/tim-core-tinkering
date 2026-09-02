package us.timinc.mc.timcore.feature.cobblemon.expall

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExpAllTest {
    @Test
    fun `preserves the published defaults`() {
        val config = ExpAll.Config()

        assertTrue(config.enabled)
        assertEquals(1.0, config.multiplier)
        assertFalse(config.force)
    }
}
