package us.timinc.mc.timcore.feature.test.blockentity

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.minecraft.MinecraftModule
import us.timinc.mc.timcore.api.minecraft.world.BlockContainer
import us.timinc.mc.timcore.feature.test.blockentity.block.CounterBlock
import us.timinc.mc.timcore.feature.test.blockentity.block.entity.CounterBlockEntity

object TestBlockEntity : AbstractFeature<TimCore, TestBlockEntity.Config>(
    TimCore,
    "test/blockentity",
    Config::class
) {
    class Config : FeatureConfig(
        enabled = false,
        debugLevel = Logger.LogLevel.SING,
    )

    object ModBlocks {
        val CLICKER_BLOCK =
            MinecraftModule.registerBlock(
                mod.modResource("clicker_block"),
                BlockContainer.WithEntity(
                    { CounterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)) },
                    ::CounterBlockEntity
                )
            )
    }

    override fun initialize() {
        ModBlocks
    }
}