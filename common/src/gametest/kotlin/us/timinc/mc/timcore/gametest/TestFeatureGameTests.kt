package us.timinc.mc.timcore.gametest

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.api.events.fishing.BobberSpawnPokemonEvent
import com.cobblemon.mod.common.api.events.pokemon.EvGainedEvent
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.pokemon.stats.SidemodEvSource
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.properties.CustomPokemonProperty
import com.cobblemon.mod.common.api.spawning.BestSpawner
import com.cobblemon.mod.common.api.spawning.detail.SpawnAction
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail
import com.cobblemon.mod.common.api.spawning.fishing.FishingSpawnCause
import com.cobblemon.mod.common.api.spawning.position.FishingSpawnablePosition
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition
import com.cobblemon.mod.common.api.spawning.selection.SpawnSelectionData
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore
import com.cobblemon.mod.common.entity.fishing.PokeRodFishingBobberEntity
import com.cobblemon.mod.common.battles.BattleBuilder
import com.cobblemon.mod.common.battles.SuccessfulBattleStart
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import com.mojang.authlib.GameProfile
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.entity.BlockEntity
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.event.Subscription
import us.timinc.mc.timcore.feature.cobblemon.customdroplogic.dropentry.TagItemDropEntry
import us.timinc.mc.timcore.feature.cobblemon.expall.ExpAll
import us.timinc.mc.timcore.feature.cobblemon.expall.event.CheckExpAllEvent
import us.timinc.mc.timcore.feature.cobblemon.expall.handler.ExpAllHandler
import us.timinc.mc.timcore.feature.cobblemon.expall.hasExpAllFor
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.PreventQuickBallSpam
import us.timinc.mc.timcore.feature.cobblemon.requirepartytofishpokemon.handler.PartyRequiredFishingHandler
import us.timinc.mc.timcore.feature.test.block.TestBlock
import us.timinc.mc.timcore.feature.test.blockentity.TestBlockEntity
import us.timinc.mc.timcore.feature.test.blockentity.block.entity.CounterBlockEntity
import us.timinc.mc.timcore.feature.test.item.TestItem
import java.util.UUID

object TestFeatureGameTests {
    const val EMPTY_TEMPLATE = "tim_core_gametest:empty"

    fun registersTestItems(helper: GameTestHelper) {
        helper.assertTrue(
            BuiltInRegistries.ITEM.getKey(TestItem.ModItems.BASIC_TEST_ITEM.item) ==
                TimCore.modResource("basic_test_item"),
            "The basic test item was not registered by its feature",
        )
        helper.assertTrue(
            BuiltInRegistries.ITEM.getKey(TestItem.ModItems.CREATIVE_TAB_TEST_ITEM.item) ==
                TimCore.modResource("creative_tab_test_item"),
            "The creative-tab test item was not registered by its feature",
        )
        helper.succeed()
    }

    fun registersTestBlocks(helper: GameTestHelper) {
        val basicBlock = TestBlock.ModBlocks.BASIC_TEST_BLOCK
        helper.assertTrue(
            BuiltInRegistries.BLOCK.getKey(basicBlock.block) == TimCore.modResource("basic_test_block"),
            "The basic test block was not registered by its feature",
        )
        helper.assertTrue(
            BuiltInRegistries.ITEM.getKey(basicBlock.item!!) == TimCore.modResource("basic_test_block"),
            "The basic test block item was not registered by its feature",
        )

        val blockWithoutItem = TestBlock.ModBlocks.BLOCK_WITHOUT_ITEM
        val blockWithoutItemId = TimCore.modResource("block_without_item")
        helper.assertTrue(
            BuiltInRegistries.BLOCK.getKey(blockWithoutItem.block) == blockWithoutItemId,
            "The itemless test block was not registered by its feature",
        )
        helper.assertTrue(
            !BuiltInRegistries.ITEM.containsKey(blockWithoutItemId),
            "The itemless test block unexpectedly registered a block item",
        )
        helper.succeed()
    }

    fun registersTestBlockEntityType(helper: GameTestHelper) {
        val clickerBlock = TestBlockEntity.ModBlocks.CLICKER_BLOCK
        helper.assertTrue(
            BuiltInRegistries.BLOCK.getKey(clickerBlock.block) == TimCore.modResource("clicker_block"),
            "The clicker block was not registered by its feature",
        )
        helper.assertTrue(
            BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(clickerBlock.blockEntityType) ==
                TimCore.modResource("clicker_block"),
            "The clicker block entity type was not registered by its feature",
        )
        helper.succeed()
    }

    fun createsTestBlockEntity(helper: GameTestHelper) {
        val clickerBlock = TestBlockEntity.ModBlocks.CLICKER_BLOCK
        val pos = BlockPos(1, 1, 1)
        helper.setBlock(pos, clickerBlock.block)

        val blockEntity: BlockEntity = helper.getBlockEntity(pos)
        helper.assertTrue(
            blockEntity is CounterBlockEntity,
            "Placing the clicker block did not create a CounterBlockEntity",
        )
        helper.assertTrue(
            blockEntity.type === clickerBlock.blockEntityType,
            "The placed counter used the wrong block entity type",
        )
        helper.succeed()
    }

    fun registersTagItemDropEntry(helper: GameTestHelper) {
        helper.assertTrue(
            DropEntry.getByName("item_tag") === TagItemDropEntry::class.java,
            "Custom drop logic did not register the item_tag drop entry with Cobblemon",
        )
        helper.succeed()
    }

    fun multipliesEvGainThroughCobblemonEvent(helper: GameTestHelper) {
        val pokemon = Pokemon()
        val event = EvGainedEvent.Pre(
            Stats.ATTACK,
            7,
            SidemodEvSource(TimCore.modId, pokemon),
        )

        CobblemonEvents.EV_GAINED_EVENT_PRE.post(event)

        helper.assertTrue(
            event.amount == 3,
            "The configured 0.5 EV multiplier produced ${event.amount} EVs instead of 3",
        )
        helper.assertTrue(
            !event.isCanceled,
            "A non-zero multiplied EV gain was unexpectedly canceled",
        )
        helper.succeed()
    }

    fun awardsExpAllExperience(helper: GameTestHelper) {
        val player = ServerPlayer(
            helper.level.server,
            helper.level,
            GameProfile(UUID.randomUUID(), "tim-core-exp-all-gametest"),
            ClientInformation.createDefault(),
        )
        helper.assertTrue(
            player.inventory.add(ItemStack(TestItem.ModItems.BASIC_TEST_ITEM.item)),
            "The Exp All test item could not be added to the player's inventory",
        )

        val participant = Pokemon()
        val recipient = Pokemon()
        val deniedByHook = Pokemon()
        val expShareHolder = Pokemon().apply {
            swapHeldItem(ItemStack(CobblemonItems.EXP_SHARE), decrement = false)
        }
        val playerParty = PlayerPartyStore(player.uuid).apply {
            add(participant)
            add(recipient)
            add(deniedByHook)
            add(expShareHolder)
        }
        val opponent = Pokemon()
        val battleStart = BattleBuilder.pve(
            player = player,
            pokemonEntity = PokemonEntity(helper.level, opponent),
            fleeDistance = -1F,
            party = playerParty,
        )
        helper.assertTrue(battleStart is SuccessfulBattleStart, "Cobblemon did not start the Exp All test battle")

        val battle = (battleStart as SuccessfulBattleStart).battle
        val playerActor = battle.actors.single { it.uuid == player.uuid }
        val opponentActor = battle.actors.single { it.uuid == opponent.uuid }
        val participantBattlePokemon = playerActor.pokemonList.single { it.originalPokemon === participant }
        val recipientBattlePokemon = playerActor.pokemonList.single { it.originalPokemon === recipient }
        val opponentBattlePokemon = opponentActor.pokemonList.single()
        participantBattlePokemon.facedOpponents += opponentBattlePokemon

        helper.assertTrue(
            player.hasExpAllFor(recipient),
            "An item in #tim_core:exp_all did not grant Exp All access",
        )
        val denialSubscription = Subscription<CheckExpAllEvent, Unit>(
            TimCore.modResource("gametest/exp_all/deny"),
        ) { event ->
            if (event.pokemon === deniedByHook) event.hasExpAll = false
        }
        ExpAll.Events.CHECK_ELIGIBILITY.subscribe(denialSubscription)

        val participantExperience = participant.experience
        val recipientExperience = recipient.experience
        val deniedExperience = deniedByHook.experience
        val expShareExperience = expShareHolder.experience
        val expectedAward = Cobblemon.experienceCalculator.calculate(
            recipientBattlePokemon,
            opponentBattlePokemon,
            ExpAll.config.values.multiplier,
        )

        try {
            ExpAllHandler.awardExperience(
                BattleVictoryEvent(battle, listOf(playerActor), listOf(opponentActor), false),
            ) { player }

            helper.assertTrue(expectedAward > 0, "Cobblemon calculated a non-positive Exp All test award")
            helper.assertTrue(
                recipient.experience == recipientExperience + expectedAward,
                "The eligible non-participant did not receive the configured Exp All award",
            )
            helper.assertTrue(
                participant.experience == participantExperience,
                "A participating Pokémon received duplicate Exp All experience",
            )
            helper.assertTrue(
                deniedByHook.experience == deniedExperience,
                "The eligibility hook did not prevent an Exp All award",
            )
            helper.assertTrue(
                expShareHolder.experience == expShareExperience,
                "An Exp Share holder received duplicate Exp All experience",
            )
            helper.succeed()
        } finally {
            ExpAll.Events.CHECK_ELIGIBILITY.unsubscribe(denialSubscription)
            if (!battle.ended) battle.stop()
        }
    }

    fun requiresPartyToFishPokemon(helper: GameTestHelper) {
        val feedback = mutableListOf<Component>()
        val player = object : ServerPlayer(
                helper.level.server,
                helper.level,
                GameProfile(UUID.randomUUID(), "tim-core-fishing-gametest"),
                ClientInformation.createDefault(),
            ) {
                override fun sendSystemMessage(message: Component) {
                    feedback += message
                }
            }
        val party = player.party()
        helper.assertTrue(party.isEmpty(), "The fishing test player unexpectedly started with a Pokémon")

        val rod = ItemStack(CobblemonItems.POKE_ROD)
        val bobber = PokeRodFishingBobberEntity(
            player,
            BuiltInRegistries.ITEM.getKey(CobblemonItems.POKE_ROD),
            ItemStack.EMPTY,
            helper.level,
            0,
            0,
            rod,
        )
        val spawnAction = createFishingSpawnAction(helper, player, rod)

        val emptyPartyEvent = BobberSpawnPokemonEvent.Pre(bobber, spawnAction, rod)
        CobblemonEvents.BOBBER_SPAWN_POKEMON_PRE.post(emptyPartyEvent)
        helper.assertTrue(
            emptyPartyEvent.isCanceled,
            "A player with an empty party was allowed to fish a Pokémon",
        )
        helper.assertTrue(
            feedback.singleOrNull() == Component.translatable(PartyRequiredFishingHandler.FEEDBACK_KEY),
            "An empty-party fishing attempt did not send the expected feedback",
        )

        party.add(Pokemon())
        val populatedPartyEvent = BobberSpawnPokemonEvent.Pre(bobber, spawnAction, rod)
        CobblemonEvents.BOBBER_SPAWN_POKEMON_PRE.post(populatedPartyEvent)
        helper.assertTrue(
            !populatedPartyEvent.isCanceled,
            "A player with a Pokémon in their party was prevented from fishing",
        )
        helper.assertTrue(feedback.size == 1, "An allowed fishing attempt unexpectedly sent feedback")
        helper.succeed()
    }

    private fun createFishingSpawnAction(
        helper: GameTestHelper,
        player: ServerPlayer,
        rod: ItemStack,
    ): SpawnAction<*> {
        val cause = FishingSpawnCause(BestSpawner.fishingSpawner, player, rod, 0)
        val position = FishingSpawnablePosition(cause, helper.level, BlockPos(1, 1, 1), mutableListOf())
        val detail = object : SpawnDetail() {
            override val type = "tim_core_gametest"

            override fun createSpawnAction(
                spawnablePosition: SpawnablePosition,
                bucket: String,
                selectionData: SpawnSelectionData,
            ): SpawnAction<*> = error("The fishing GameTest does not select this synthetic spawn detail.")
        }

        return object : SpawnAction<Entity>(position, "common", detail) {
            override fun run(): Entity? = null
        }
    }

    fun persistsQuickBallImmunityProperty(helper: GameTestHelper) {
        val immunityProperty = PreventQuickBallSpam.PokemonProperties.immuneToQuickBall
        helper.assertTrue(
            CustomPokemonProperty.properties.any { it === immunityProperty },
            "The quick-ball immunity property was not registered with Cobblemon",
        )

        val pokemon = Pokemon()
        helper.assertTrue(
            !immunityProperty.getValue(pokemon),
            "A new Pokemon was unexpectedly immune to Quick Balls",
        )

        val parsedProperty = immunityProperty.fromString("yes")
        parsedProperty.apply(pokemon)
        helper.assertTrue(
            immunityProperty.getValue(pokemon),
            "Quick-ball immunity was not stored in the Pokemon's persistent data",
        )
        helper.assertTrue(
            parsedProperty.matches(pokemon),
            "The applied quick-ball immunity property did not match the Pokemon",
        )
        helper.succeed()
    }

    fun ignoresCatchRateEventsWithoutQuickBallBonus(helper: GameTestHelper) {
        val thrower = helper.makeMockPlayer(GameType.SURVIVAL)
        val pokemon = Pokemon()
        val pokemonEntity = PokemonEntity(helper.level, pokemon)
        val initialCatchRate = 120F

        val regularBallEvent = PokemonCatchRateEvent(
            thrower,
            EmptyPokeBallEntity(PokeBalls.POKE_BALL, helper.level, thrower),
            pokemonEntity,
            initialCatchRate,
        )
        CobblemonEvents.POKEMON_CATCH_RATE.post(regularBallEvent)
        helper.assertTrue(
            regularBallEvent.catchRate == initialCatchRate,
            "A non-Quick Ball unexpectedly changed the catch rate",
        )
        helper.assertTrue(
            !PreventQuickBallSpam.PokemonProperties.immuneToQuickBall.getValue(pokemon),
            "A non-Quick Ball unexpectedly granted Quick Ball immunity",
        )

        val outOfBattleQuickBallEvent = PokemonCatchRateEvent(
            thrower,
            EmptyPokeBallEntity(PokeBalls.QUICK_BALL, helper.level, thrower),
            pokemonEntity,
            initialCatchRate,
        )
        CobblemonEvents.POKEMON_CATCH_RATE.post(outOfBattleQuickBallEvent)
        helper.assertTrue(
            outOfBattleQuickBallEvent.catchRate == initialCatchRate,
            "An out-of-battle Quick Ball unexpectedly changed the catch rate",
        )
        helper.assertTrue(
            !PreventQuickBallSpam.PokemonProperties.immuneToQuickBall.getValue(pokemon),
            "An out-of-battle Quick Ball unexpectedly granted immunity",
        )
        helper.succeed()
    }

    fun preventsRepeatedTurnOneQuickBallBonus(helper: GameTestHelper) {
        val thrower = ServerPlayer(
            helper.level.server,
            helper.level,
            GameProfile(UUID.randomUUID(), "tim-core-gametest"),
            ClientInformation.createDefault(),
        )
        val playerParty = PlayerPartyStore(thrower.uuid).apply { add(Pokemon()) }
        val pokemon = Pokemon()
        val pokemonEntity = PokemonEntity(helper.level, pokemon)
        val battleStart = BattleBuilder.pve(
            player = thrower,
            pokemonEntity = pokemonEntity,
            fleeDistance = -1F,
            party = playerParty,
        )
        helper.assertTrue(
            battleStart is SuccessfulBattleStart,
            "Cobblemon did not start the turn-one test battle",
        )

        val battle = (battleStart as SuccessfulBattleStart).battle
        try {
            battle.turn(1)
            val quickBall = PokeBalls.QUICK_BALL
            val quickBallEntity = EmptyPokeBallEntity(quickBall, helper.level, thrower)
            val initialCatchRate = 120F
            val quickBallFactor = quickBall.catchRateModifier.modifyCatchRate(1F, thrower, pokemon)
            helper.assertTrue(
                quickBallFactor == 5F,
                "Cobblemon's turn-one Quick Ball modifier was unexpectedly $quickBallFactor instead of 5",
            )

            val firstQuickBallEvent = PokemonCatchRateEvent(
                thrower,
                quickBallEntity,
                pokemonEntity,
                initialCatchRate,
            )
            CobblemonEvents.POKEMON_CATCH_RATE.post(firstQuickBallEvent)
            helper.assertTrue(
                firstQuickBallEvent.catchRate == initialCatchRate,
                "A Pokemon's first turn-one Quick Ball unexpectedly lost its bonus",
            )
            helper.assertTrue(
                PreventQuickBallSpam.PokemonProperties.immuneToQuickBall.getValue(pokemon),
                "The first boosted Quick Ball did not grant immunity to later attempts",
            )

            val repeatedQuickBallEvent = PokemonCatchRateEvent(
                thrower,
                quickBallEntity,
                pokemonEntity,
                initialCatchRate,
            )
            CobblemonEvents.POKEMON_CATCH_RATE.post(repeatedQuickBallEvent)
            helper.assertTrue(
                repeatedQuickBallEvent.catchRate == initialCatchRate / quickBallFactor,
                "A repeated turn-one Quick Ball did not remove its bonus from the catch rate",
            )
            helper.assertTrue(
                quickBall.catchRateModifier.modifyCatchRate(repeatedQuickBallEvent.catchRate, thrower, pokemon) ==
                    initialCatchRate,
                "Cobblemon's Quick Ball modifier did not restore the repeated attempt to the base catch rate",
            )
            helper.succeed()
        } finally {
            if (!battle.ended) battle.stop()
        }
    }
}
