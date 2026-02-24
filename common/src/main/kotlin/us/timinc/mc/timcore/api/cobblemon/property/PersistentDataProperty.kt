package us.timinc.mc.timcore.api.cobblemon.property

import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.properties.BooleanProperty
import com.cobblemon.mod.common.pokemon.properties.FloatProperty
import com.cobblemon.mod.common.pokemon.properties.IntProperty
import com.cobblemon.mod.common.pokemon.properties.StringProperty
import us.timinc.mc.timcore.api.cobblemon.extension.getIdentifier
import us.timinc.mc.timcore.api.cobblemon.extension.getModPersistentData
import us.timinc.mc.timcore.api.extension.getBooleanOrNull
import us.timinc.mc.timcore.api.extension.getFloatOrNull
import us.timinc.mc.timcore.api.extension.getIntOrNull
import us.timinc.mc.timcore.api.extension.getStringOrNull
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.api.mod.AbstractMod

/**
 * A collection of custom Pokémon properties whose values are stored in the Pokémon's persistent data, in a compound
 * tag keyed to the mod's ID.
 *
 * @author Timothy Metcalfe
 * @sample us.timinc.mc.timcore.feature.preventquickballspam.PreventQuickBallSpam.PokemonProperties
 */
object PersistentDataProperty {
    /**
     * A Boolean value backed by a Pokémon's persistent data.
     *
     * @author Timothy Metcalfe
     * @sample us.timinc.mc.timcore.feature.preventquickballspam.PreventQuickBallSpam.PokemonProperties
     */
    class Boolean private constructor(val modId: kotlin.String, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<BooleanProperty> {
        constructor(mod: AbstractMod<*>, vararg keys: kotlin.String) : this(mod.modId, keys.toList())
        constructor(modId: kotlin.String, vararg keys: kotlin.String) : this(modId, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun toString(): kotlin.String = "$modId:${keys.first()}"

        override fun examples(): Collection<kotlin.String> = setOf("yes", "no")

        override fun fromString(value: kotlin.String?): BooleanProperty {
            val logger = LoggerScope.current()
            logger.sing("Parsing PersistentDataProperty.Boolean $this from string value $value.")
            val actualValue = value == null || listOf("yes", "true").contains(value)
            logger.sing("Parsed to $actualValue.")
            return BooleanProperty(
                keys.first(),
                actualValue,
                ::pokemonApplicator,
                ::entityApplicator,
                ::pokemonMatcher,
                ::entityMatcher
            )
        }

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.Boolean) {
            val logger = LoggerScope.current()
            logger.sing("Applying PersistentDataProperty.Boolean $this as $value to ${pokemon.getIdentifier()}.")
            pokemon.getModPersistentData(modId).putBoolean(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.Boolean) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.Boolean = pokemon.getModPersistentData(modId).let { modData ->
            modData.contains(keys.first()) && (modData.getBooleanOrNull(keys.first()) == true)
        }

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.Boolean): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.Boolean): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }

    /**
     * A Float value backed by a Pokémon's persistent data.
     *
     * @author Timothy Metcalfe
     */
    class Float private constructor(val modId: kotlin.String, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<FloatProperty> {
        constructor(mod: AbstractMod<*>, vararg keys: kotlin.String) : this(mod.modId, keys.toList())
        constructor(modId: kotlin.String, vararg keys: kotlin.String) : this(modId, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun toString(): kotlin.String = "$modId:${keys.first()}"

        override fun examples(): Collection<kotlin.String> = (0..5).map { (it * 0.2).toString() }

        override fun fromString(value: kotlin.String?): FloatProperty {
            val logger = LoggerScope.current()
            logger.sing("Parsing PersistentDataProperty.Float $this from string value $value.")
            val actualValue = value?.toFloat() ?: 0F
            logger.sing("Parsed to $actualValue.")
            return FloatProperty(
                keys.first(),
                actualValue,
                ::pokemonApplicator,
                ::entityApplicator,
                ::pokemonMatcher,
                ::entityMatcher
            )
        }

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.Float) {
            val logger = LoggerScope.current()
            logger.sing("Applying PersistentDataProperty.Float $this as $value to ${pokemon.getIdentifier()}.")
            pokemon.getModPersistentData(modId).putFloat(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.Float) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.Float? = pokemon.getModPersistentData(modId).getFloatOrNull(keys.first())

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.Float): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.Float): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }

    /**
     * An Int value backed by a Pokémon's persistent data.
     *
     * @author Timothy Metcalfe
     */
    class Int private constructor(val modId: kotlin.String, override val keys: Iterable<kotlin.String>) :
        CustomPokemonPropertyType<IntProperty> {
        constructor(mod: AbstractMod<*>, vararg keys: kotlin.String) : this(mod.modId, keys.toList())
        constructor(modId: kotlin.String, vararg keys: kotlin.String) : this(modId, keys.toList())

        override val needsKey: kotlin.Boolean = true

        override fun toString() = "$modId:${keys.first()}"

        override fun examples(): Collection<kotlin.String> = (0..5).map { (it * 20).toString() }

        override fun fromString(value: kotlin.String?): IntProperty {
            val logger = LoggerScope.current()
            logger.sing("Parsing PersistentDataProperty.Int $this from string value $value.")
            val actualValue = value?.toInt() ?: 0
            logger.sing("Parsed to $actualValue.")
            return IntProperty(
                keys.first(),
                actualValue,
                ::pokemonApplicator,
                ::entityApplicator,
                ::pokemonMatcher,
                ::entityMatcher
            )
        }

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.Int) {
            val logger = LoggerScope.current()
            logger.sing("Applying PersistentDataProperty.Int $this from string value $value.")
            pokemon.getModPersistentData(modId).putInt(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.Int) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.Int? = pokemon.getModPersistentData(modId).getIntOrNull(keys.first())

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.Int): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.Int): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }

    /**
     * A String value backed by a Pokémon's persistent data.
     *
     * @author Timothy Metcalfe
     */
    class String private constructor(
        val modId: kotlin.String,
        val examples: Set<kotlin.String>,
        override val keys: Iterable<kotlin.String>
    ) :
        CustomPokemonPropertyType<StringProperty> {
        constructor(mod: AbstractMod<*>, examples: Set<kotlin.String>, vararg keys: kotlin.String) : this(
            mod.modId,
            examples,
            keys.toList()
        )

        constructor(modId: kotlin.String, examples: Set<kotlin.String>, vararg keys: kotlin.String) : this(
            modId,
            examples,
            keys.toList()
        )

        override val needsKey: kotlin.Boolean = true

        override fun toString() = "$modId:${keys.first()}"

        override fun examples(): Collection<kotlin.String> = examples

        override fun fromString(value: kotlin.String?): StringProperty {
            val logger = LoggerScope.current()
            logger.sing("Parsing PersistentDataProperty.String $this from string value $value.")
            val actualValue = value ?: ""
            logger.sing("Parsed to $actualValue.")
            return StringProperty(
                keys.first(),
                actualValue,
                ::pokemonApplicator,
                ::pokemonMatcher,
            )
        }

        fun pokemonApplicator(pokemon: Pokemon, value: kotlin.String) {
            val logger = LoggerScope.current()
            logger.sing("Applying PersistentDataProperty.String $this from string value $value.")
            pokemon.getModPersistentData(modId).putString(keys.first(), value)
        }

        fun entityApplicator(entity: PokemonEntity, value: kotlin.String) {
            pokemonApplicator(entity.pokemon, value)
        }

        fun getValue(pokemon: Pokemon): kotlin.String? = pokemon.getModPersistentData(modId).getStringOrNull(keys.first())

        fun pokemonMatcher(pokemon: Pokemon, value: kotlin.String): kotlin.Boolean = getValue(pokemon) == value

        fun entityMatcher(entity: PokemonEntity, value: kotlin.String): kotlin.Boolean =
            pokemonMatcher(entity.pokemon, value)
    }
}