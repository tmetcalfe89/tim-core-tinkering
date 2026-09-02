package us.timinc.mc.timcore.feature.cobblemon.expall.handler

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExpAllHandlerTest {
    @Test
    fun `accepts finite non-negative multipliers`() {
        assertTrue(ExpAllHandler.isValidMultiplier(0.0))
        assertTrue(ExpAllHandler.isValidMultiplier(1.0))
        assertTrue(ExpAllHandler.isValidMultiplier(2.5))
    }

    @Test
    fun `rejects invalid multipliers`() {
        assertFalse(ExpAllHandler.isValidMultiplier(-1.0))
        assertFalse(ExpAllHandler.isValidMultiplier(Double.NaN))
        assertFalse(ExpAllHandler.isValidMultiplier(Double.POSITIVE_INFINITY))
    }
}
