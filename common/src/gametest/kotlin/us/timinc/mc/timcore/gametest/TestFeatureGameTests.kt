package us.timinc.mc.timcore.gametest

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.level.block.entity.BlockEntity
import us.timinc.mc.timcore.TimCore
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
}
