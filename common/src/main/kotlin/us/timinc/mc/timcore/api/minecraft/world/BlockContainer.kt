package us.timinc.mc.timcore.api.minecraft.world

import com.mojang.datafixers.types.Type
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState

open class BlockContainer<B : Block>(
    open val blockBuilder: () -> B,
    open val itemProperties: Item.Properties = Item.Properties(),
    open val ignoreItem: Boolean = false,
    open val tab: ResourceKey<CreativeModeTab>? = null,
) {
    class Basic(
        val blockProperties: BlockBehaviour.Properties,
        override val itemProperties: Item.Properties = Item.Properties(),
        override val ignoreItem: Boolean = false,
        override val tab: ResourceKey<CreativeModeTab>? = null,
    ) : BlockContainer<Block>(
        { Block(blockProperties) },
        itemProperties,
        ignoreItem,
        tab
    )

    class WithEntity<B : Block, E : BlockEntity>(
        override val blockBuilder: () -> B,
        val entityBuilder: (BlockPos, BlockState) -> E,
        override val itemProperties: Item.Properties = Item.Properties(),
        override val ignoreItem: Boolean = false,
        override val tab: ResourceKey<CreativeModeTab>? = null,
    ) : BlockContainer<B>(
        blockBuilder,
        itemProperties,
        ignoreItem,
        tab,
    ) {
        val blockEntityType: BlockEntityType<E> by lazy {
            BlockEntityType.Builder<E>.of<E>(entityBuilder, block).build(null as Type<*>?)
        }
    }

    val block by lazy {
        blockBuilder()
    }
    val item by lazy {
        if (ignoreItem) return@lazy null
        BlockItem(block, itemProperties)
    }
}