package us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.extension

import com.cobblemon.mod.common.pokemon.Pokemon
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.PreventQuickBallSpam

fun Pokemon.isImmuneToQuickBall(): Boolean =
    PreventQuickBallSpam.PokemonProperties.immuneToQuickBall.pokemonMatcher(this, true)

fun Pokemon.setImmuneToQuickBall() {
    PreventQuickBallSpam.PokemonProperties.immuneToQuickBall.pokemonApplicator(this, true)
}