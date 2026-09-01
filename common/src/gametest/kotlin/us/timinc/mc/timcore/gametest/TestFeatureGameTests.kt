package us.timinc.mc.timcore.gametest

import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.properties.CustomPokemonProperty
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.entity.BlockEntity
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.feature.cobblemon.customdroplogic.dropentry.TagItemDropEntry
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.PreventQuickBallSpam
import us.timinc.mc.timcore.feature.test.block.TestBlock
import us.timinc.mc.timcore.feature.test.blockentity.TestBlockEntity
import us.timinc.mc.timcore.feature.test.blockentity.block.entity.CounterBlockEntity
import us.timinc.mc.timcore.feature.test.item.TestItem

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
}
