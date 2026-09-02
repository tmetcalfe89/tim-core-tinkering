package us.timinc.mc.timcore.feature.cobblemon.expall

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.event.Event
import us.timinc.mc.timcore.api.event.Subscribable
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.feature.cobblemon.expall.event.CheckExpAllEvent
import us.timinc.mc.timcore.feature.cobblemon.expall.handler.ExpAllHandler

object ExpAll : AbstractFeature<TimCore, ExpAll.Config>(
    TimCore,
    "exp_all",
    Config::class,
    setOf("cobblemon"),
) {
    class Config(
        enabled: Boolean = true,
        debugLevel: Logger.LogLevel = Logger.LogLevel.WARN,
        val multiplier: Double = 1.0,
        val force: Boolean = false,
    ) : FeatureConfig(enabled, debugLevel)

    object Tags {
        @JvmField
        val EXP_ALL: TagKey<Item> = TagKey.create(Registries.ITEM, TimCore.modResource("exp_all"))
    }

    private val checkEligibilityEvent = Event<CheckExpAllEvent, Unit>()

    object Events {
        @JvmField
        val CHECK_ELIGIBILITY: Subscribable<CheckExpAllEvent, Unit> = checkEligibilityEvent.asSubscribable()
    }

    @JvmStatic
    fun hasExpAllFor(player: ServerPlayer, pokemon: Pokemon? = null): Boolean {
        val hasTaggedItem = player.inventory.items.any { it.`is`(Tags.EXP_ALL) }
        val event = CheckExpAllEvent(player, config.values.force || hasTaggedItem, pokemon)
        checkEligibilityEvent.fire(event)
        return event.hasExpAll
    }

    override fun initialize() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.HIGHEST, ExpAllHandler::handle)
    }
}
