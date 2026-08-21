package us.timinc.mc.timcore.api.minecraft

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import us.timinc.mc.timcore.api.minecraft.world.BlockContainer
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer
import us.timinc.mc.timcore.api.mod.PlatformBits

/** Keeps registration state isolated from the global module lifecycle. */
internal class MinecraftRegistrationQueue {
    private val items: MutableMap<ResourceLocation, ItemContainer<*>> = mutableMapOf()
    private val blocks: MutableMap<ResourceLocation, BlockContainer<*>> = mutableMapOf()
    private var initialized: Boolean = false

    fun initialize(
        platformBits: PlatformBits,
        onRegisteringItems: (Int) -> Unit = {},
        onRegisteringBlocks: (Int) -> Unit = {},
    ) {
        if (items.isNotEmpty()) {
            onRegisteringItems(items.size)
            platformBits.registerItems(items)
        }
        if (blocks.isNotEmpty()) {
            onRegisteringBlocks(blocks.size)
            platformBits.registerBlocks(blocks)
        }
        initialized = true
    }

    fun <T : Item> registerItem(
        id: ResourceLocation,
        container: ItemContainer<T>,
        beforeRegistration: () -> Unit = {},
    ): ItemContainer<T> {
        if (initialized) throw IllegalStateException("Minecraft module already initialized.")
        if (items.containsKey(id)) throw IllegalStateException("Item $id already registered.")

        beforeRegistration()
        items[id] = container
        return container
    }

    fun <T : Block, C : BlockContainer<T>> registerBlock(
        id: ResourceLocation,
        container: C,
        beforeRegistration: () -> Unit = {},
    ): C {
        if (initialized) throw IllegalStateException("Minecraft module already initialized.")
        if (blocks.containsKey(id)) throw IllegalStateException("Block $id already registered.")

        beforeRegistration()
        blocks[id] = container
        return container
    }
}
