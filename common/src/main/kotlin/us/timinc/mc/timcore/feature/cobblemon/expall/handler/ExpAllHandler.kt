package us.timinc.mc.timcore.feature.cobblemon.expall.handler

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.api.tags.CobblemonItemTags
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.timcore.feature.cobblemon.expall.ExpAll
import us.timinc.mc.timcore.feature.cobblemon.expall.hasExpAllFor

object ExpAllHandler {
    fun handle(event: BattleVictoryEvent) = awardExperience(event) { battlePokemon ->
        battlePokemon.originalPokemon.getOwnerPlayer()
    }

    @JvmSynthetic
    fun awardExperience(
        event: BattleVictoryEvent,
        resolveOwner: (BattlePokemon) -> ServerPlayer?,
    ) {
        ExpAll.withOperationContext {
            val multiplier = config.multiplier
            if (!isValidMultiplier(multiplier)) {
                logger.alert("Exp All multiplier must be a finite, non-negative number; ignoring $multiplier.")
                return
            }

            logger.sing("Reviewing battle victory for Exp All awards.")
            for (winner in event.winners) {
                for (winningPokemon in winner.pokemonList) {
                    val pokemon = winningPokemon.effectedPokemon
                    val owner = resolveOwner(winningPokemon)
                    if (owner == null) {
                        logger.sing("Skipping ${pokemon.uuid}; it is not owned by an online player.")
                        continue
                    }
                    if (!owner.hasExpAllFor(pokemon)) {
                        logger.sing("Skipping ${pokemon.uuid}; ${owner.gameProfile.name} has no Exp All access.")
                        continue
                    }
                    if (pokemon.heldItem().`is`(CobblemonItemTags.EXPERIENCE_SHARE)) {
                        logger.sing("Skipping ${pokemon.uuid}; Cobblemon will award its Exp Share experience.")
                        continue
                    }

                    for (loser in event.losers) {
                        for (losingPokemon in loser.pokemonList) {
                            if (losingPokemon in winningPokemon.facedOpponents) {
                                logger.sing(
                                    "Skipping ${pokemon.uuid} against ${losingPokemon.effectedPokemon.uuid}; " +
                                        "Cobblemon will award participant experience.",
                                )
                                continue
                            }

                            val experience = Cobblemon.experienceCalculator.calculate(
                                winningPokemon,
                                losingPokemon,
                                multiplier,
                            )
                            if (experience <= 0) {
                                logger.sing("Skipping a non-positive Exp All award of $experience for ${pokemon.uuid}.")
                                continue
                            }

                            logger.sing("Awarding $experience Exp All experience to ${pokemon.uuid}.")
                            winner.awardExperience(winningPokemon, experience)
                        }
                    }
                }
            }
        }
    }

    internal fun isValidMultiplier(multiplier: Double): Boolean = multiplier.isFinite() && multiplier >= 0.0
}
