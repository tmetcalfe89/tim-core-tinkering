package us.timinc.mc.timcore.feature.cobblemon.expall.event

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer

data class CheckExpAllEvent(
    val player: ServerPlayer,
    var hasExpAll: Boolean,
    val pokemon: Pokemon? = null,
)
