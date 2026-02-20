package us.timinc.mc.timcore.api.cobblemon.extension

import com.cobblemon.mod.common.pokemon.Pokemon

fun Pokemon.getIdentifier() = with(this) { "${getDisplayName()}_$uuid" }