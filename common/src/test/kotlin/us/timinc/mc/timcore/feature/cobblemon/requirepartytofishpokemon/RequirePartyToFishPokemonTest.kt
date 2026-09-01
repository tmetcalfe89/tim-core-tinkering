package us.timinc.mc.timcore.feature.cobblemon.requirepartytofishpokemon

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class RequirePartyToFishPokemonTest {
    @Test
    fun `preserves the original disabled default`() {
        assertFalse(RequirePartyToFishPokemon.Config().enabled)
    }
}
