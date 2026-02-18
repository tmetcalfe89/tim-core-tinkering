package us.timinc.mc.timcore.api.cobblemon.property

import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.properties.BooleanProperty
import com.cobblemon.mod.common.pokemon.properties.FloatProperty
import com.cobblemon.mod.common.pokemon.properties.IntProperty
import com.cobblemon.mod.common.pokemon.properties.StringProperty
import us.timinc.mc.timcore.api.extension.*
import us.timinc.mc.timcore.api.mod.AbstractMod

@Suppress("unused")
object PersistentDataProperty {
    fun getModData(modId: kotlin.String, pokemon: Pokemon) = pokemon.persistentData.getOrPutCompound(modId)

    class Boolean private constructor(val modId: kotlin.String, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<BooleanProperty> {
        constructor(mod: AbstractMod<*>, vararg keys: kotlin.String) : this(mod.modId, keys.toList())
        constructor(modId: kotlin.String, vararg keys: kotlin.String) : this(modId, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun examples(): Collection<kotlin.String> = setOf("yes", "no")

        override fun fromString(value: kotlin.String?) = BooleanProperty(
            keys.first(),
            listOf("yes", "true").contains(value),
            ::pokemonApplicator,
            ::entityApplicator,
            ::pokemonMatcher,
            ::entityMatcher
        )

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.Boolean) {
            getModData(modId, pokemon).putBoolean(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.Boolean) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon) = getModData(modId, pokemon).let { modData ->
            modData.contains(keys.first()) && (modData.getBooleanOrNull(keys.first()) == true)
        }

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.Boolean): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.Boolean): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }

    class Float private constructor(val modId: kotlin.String, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<FloatProperty> {
        constructor(mod: AbstractMod<*>, vararg keys: kotlin.String) : this(mod.modId, keys.toList())
        constructor(modId: kotlin.String, vararg keys: kotlin.String) : this(modId, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun examples(): Collection<kotlin.String> = (0..5).map { (it * 0.2).toString() }

        override fun fromString(value: kotlin.String?) = FloatProperty(
            keys.first(),
            value?.toFloat() ?: 0F,
            ::pokemonApplicator,
            ::entityApplicator,
            ::pokemonMatcher,
            ::entityMatcher
        )

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.Float) {
            getModData(modId, pokemon).putFloat(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.Float) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.Float? = getModData(modId, pokemon).getFloatOrNull(keys.first())

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.Float): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.Float): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }

    class Int private constructor(val modId: kotlin.String, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<IntProperty> {
        constructor(mod: AbstractMod<*>, vararg keys: kotlin.String) : this(mod.modId, keys.toList())
        constructor(modId: kotlin.String, vararg keys: kotlin.String) : this(modId, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun examples(): Collection<kotlin.String> = (0..5).map { (it * 20).toString() }

        override fun fromString(value: kotlin.String?) = IntProperty(
            keys.first(),
            value?.toInt() ?: 0,
            ::pokemonApplicator,
            ::entityApplicator,
            ::pokemonMatcher,
            ::entityMatcher
        )

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.Int) {
            getModData(modId, pokemon).putInt(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.Int) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.Int? = getModData(modId, pokemon).getIntOrNull(keys.first())

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.Int): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.Int): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }

    class String private constructor(val modId: kotlin.String, val examples: Set<kotlin.String>, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<StringProperty> {
        constructor(mod: AbstractMod<*>, examples: Set<kotlin.String>, vararg keys: kotlin.String) : this(mod.modId, examples, keys.toList())
        constructor(modId: kotlin.String, examples: Set<kotlin.String>, vararg keys: kotlin.String) : this(modId, examples, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun examples(): Collection<kotlin.String> = (0..5).map { (it * 20).toString() }

        override fun fromString(value: kotlin.String?) = StringProperty(
            keys.first(),
            value ?: "",
            ::pokemonApplicator,
            ::pokemonMatcher,
        )

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.String) {
            getModData(modId, pokemon).putString(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.String) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.String? = getModData(modId, pokemon).getStringOrNull(keys.first())

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.String): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.String): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }
}