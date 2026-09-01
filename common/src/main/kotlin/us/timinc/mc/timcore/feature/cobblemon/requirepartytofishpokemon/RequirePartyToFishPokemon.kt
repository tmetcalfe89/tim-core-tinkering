package us.timinc.mc.timcore.feature.cobblemon.requirepartytofishpokemon

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.feature.cobblemon.requirepartytofishpokemon.handler.PartyRequiredFishingHandler

object RequirePartyToFishPokemon : AbstractFeature<TimCore, RequirePartyToFishPokemon.Config>(
    TimCore,
    "require_party_to_fish_pokemon",
    Config::class,
    setOf("cobblemon"),
) {
    class Config(
        enabled: Boolean = false,
        debugLevel: Logger.LogLevel = Logger.LogLevel.WARN,
    ) : FeatureConfig(enabled, debugLevel)

    override fun initialize() {
        CobblemonEvents.BOBBER_SPAWN_POKEMON_PRE.subscribe(Priority.HIGHEST, PartyRequiredFishingHandler::handle)
    }
}
