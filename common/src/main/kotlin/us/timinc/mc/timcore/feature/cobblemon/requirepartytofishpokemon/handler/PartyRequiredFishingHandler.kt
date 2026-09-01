package us.timinc.mc.timcore.feature.cobblemon.requirepartytofishpokemon.handler

import com.cobblemon.mod.common.api.events.fishing.BobberSpawnPokemonEvent
import com.cobblemon.mod.common.util.party
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.timcore.feature.cobblemon.requirepartytofishpokemon.RequirePartyToFishPokemon

object PartyRequiredFishingHandler {
    fun handle(event: BobberSpawnPokemonEvent.Pre) {
        RequirePartyToFishPokemon.withOperationContext {
            val player = event.bobber.owner as? ServerPlayer
            if (player == null) {
                logger.warn("A Poké Rod spawn had no server player owner; allowing it.")
                return
            }

            if (!player.party().isEmpty()) {
                logger.sing("Allowing ${player.gameProfile.name} to fish a Pokémon because their party is not empty.")
                return
            }

            logger.sing("Preventing ${player.gameProfile.name} from fishing a Pokémon because their party is empty.")
            player.sendSystemMessage(Component.translatable(FEEDBACK_KEY))
            event.cancel()
        }
    }

    const val FEEDBACK_KEY = "tim_core.feedback.party_required_to_fish_pokemon"
}
