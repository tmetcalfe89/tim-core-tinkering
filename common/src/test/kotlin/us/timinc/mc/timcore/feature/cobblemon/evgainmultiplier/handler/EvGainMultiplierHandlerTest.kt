package us.timinc.mc.timcore.feature.cobblemon.evgainmultiplier.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EvGainMultiplierHandlerTest {
    @Test
    fun `leaves EV gain unchanged with the default multiplier`() {
        assertEquals(7, EvGainMultiplierHandler.multiply(7, 1.0))
    }

    @Test
    fun `supports increased EV gain`() {
        assertEquals(14, EvGainMultiplierHandler.multiply(7, 2.0))
    }

    @Test
    fun `rounds positive fractional EV gain down`() {
        assertEquals(3, EvGainMultiplierHandler.multiply(7, 0.5))
    }

    @Test
    fun `supports disabling EV gain`() {
        assertEquals(0, EvGainMultiplierHandler.multiply(7, 0.0))
    }

    @Test
    fun `clamps EV gain that exceeds the integer range`() {
        assertEquals(Int.MAX_VALUE, EvGainMultiplierHandler.multiply(Int.MAX_VALUE, 2.0))
    }

    @Test
    fun `rejects invalid multipliers`() {
        assertThrows(IllegalArgumentException::class.java) {
            EvGainMultiplierHandler.multiply(7, -1.0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            EvGainMultiplierHandler.multiply(7, Double.POSITIVE_INFINITY)
        }
    }
}
