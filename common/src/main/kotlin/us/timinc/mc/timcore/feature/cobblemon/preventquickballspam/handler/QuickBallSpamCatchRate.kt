package us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.handler

import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.PreventQuickBallSpam
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.extension.isImmuneToQuickBall
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.extension.setImmuneToQuickBall

object QuickBallSpamCatchRate {
    fun handle(evt: PokemonCatchRateEvent) {
        PreventQuickBallSpam.withOperationContext {
            val pokeBall = evt.pokeBallEntity.pokeBall
            if (pokeBall != PokeBalls.QUICK_BALL) return

            logger.sing("Quick Ball detected. Checking for immunity to its effect.")

            val pokemon = evt.pokemonEntity.pokemon

            val reversionFactor = PokeBalls.QUICK_BALL.catchRateModifier.modifyCatchRate(1.0F, evt.thrower, pokemon)
            if (reversionFactor == 1F) {
                logger.sing("Ball effect not applied (usually due to being used outside of battle). No need to revert or grant immunity.")
                return
            }

            if (pokemon.isImmuneToQuickBall()) {
                logger.sing("Pokemon is immune to quick ball.")
                logger.sing("Pre-reversion catch rate: ${evt.catchRate}.")
                evt.catchRate /= reversionFactor
                logger.sing("Post-reversion catch rate: ${evt.catchRate}.")
            } else {
                logger.sing("Pokemon is not immune; marking as immune.")
                pokemon.setImmuneToQuickBall()
            }
        }
    }
}