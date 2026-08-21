package us.timinc.mc.timcore.feature.test.block

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.minecraft.MinecraftModule
import us.timinc.mc.timcore.api.minecraft.world.BlockContainer

object TestBlock : AbstractFeature<TimCore, TestBlock.Config>(
    TimCore,
    "test/block",
    Config::class,
) {
    class Config : FeatureConfig(
        enabled = false,
        debugLevel = Logger.LogLevel.SING,
    )

    object ModBlocks {
        val BASIC_TEST_BLOCK =
            MinecraftModule.registerBlock(
                mod.modResource("basic_test_block"),
                BlockContainer.Basic(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                )
            )
        val BLOCK_WITHOUT_ITEM =
            MinecraftModule.registerBlock(
                mod.modResource("block_without_item"),
                BlockContainer.Basic(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE),
                    ignoreItem = true,
                )
            )
    }

    override fun initialize() {
        ModBlocks
    }
}
