package us.timinc.mc.timcore.feature.cobblemon.expall

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer

fun ServerPlayer.hasExpAllFor(pokemon: Pokemon? = null): Boolean = ExpAll.hasExpAllFor(this, pokemon)
