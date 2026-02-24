package us.timinc.mc.timcore.api.cobblemon.extension

import com.cobblemon.mod.common.pokemon.Pokemon
import us.timinc.mc.timcore.api.extension.getOrPutCompound

/**
 * A convenient identifier for a given Pokemon, with its display name for general and its UUID for specific. Useful for
 * logs.
 *
 * @author Timothy Metcalfe
 */
fun Pokemon.getIdentifier() = with(this) { "${getDisplayName()}_$uuid" }

fun Pokemon.getModPersistentData(modId: String) = this.persistentData.getOrPutCompound(modId)