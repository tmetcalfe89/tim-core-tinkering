package us.timinc.mc.timcore.api.minecraft

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import us.timinc.mc.timcore.api.minecraft.world.BlockContainer
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer
import us.timinc.mc.timcore.api.mod.PlatformBits

class MinecraftRegistrationQueueTest {
    private class RecordingPlatformBits : PlatformBits() {
        override val platformName: String = "Test"
        val itemRegistrations = mutableListOf<Map<ResourceLocation, ItemContainer<*>>>()
        val blockRegistrations = mutableListOf<Map<ResourceLocation, BlockContainer<*>>>()

        override fun isModPresent(modId: String): Boolean = false

        override fun registerItems(items: MutableMap<ResourceLocation, ItemContainer<*>>) {
            itemRegistrations.add(items.toMap())
        }

        override fun registerBlocks(blocks: MutableMap<ResourceLocation, BlockContainer<*>>) {
            blockRegistrations.add(blocks.toMap())
        }
    }

    private fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("test", path)

    private fun itemContainer(): ItemContainer<Item> =
        ItemContainer(itemBuilder = { throw AssertionError("Registration should not construct the item") })

    private fun blockContainer(): BlockContainer<Block> =
        BlockContainer(
            blockBuilder = { throw AssertionError("Registration should not construct the block") },
            ignoreItem = true,
        )

    @Test
    fun `initialization hands queued items and blocks to the platform`() {
        val registrations = MinecraftRegistrationQueue()
        val platform = RecordingPlatformBits()
        val itemId = id("item")
        val blockId = id("block")
        val item = itemContainer()
        val block = blockContainer()

        assertSame(item, registrations.registerItem(itemId, item))
        assertSame(block, registrations.registerBlock(blockId, block))
        registrations.initialize(platform)

        assertEquals(listOf(mapOf(itemId to item)), platform.itemRegistrations)
        assertEquals(listOf(mapOf(blockId to block)), platform.blockRegistrations)
    }

    @Test
    fun `duplicate item ids are rejected`() {
        val registrations = MinecraftRegistrationQueue()
        val itemId = id("duplicate_item")
        registrations.registerItem(itemId, itemContainer())

        val exception = assertThrows(IllegalStateException::class.java) {
            registrations.registerItem(itemId, itemContainer())
        }

        assertEquals("Item $itemId already registered.", exception.message)
    }

    @Test
    fun `duplicate block ids are rejected`() {
        val registrations = MinecraftRegistrationQueue()
        val blockId = id("duplicate_block")
        registrations.registerBlock(blockId, blockContainer())

        val exception = assertThrows(IllegalStateException::class.java) {
            registrations.registerBlock(blockId, blockContainer())
        }

        assertEquals("Block $blockId already registered.", exception.message)
    }

    @Test
    fun `registrations are rejected after initialization`() {
        val registrations = MinecraftRegistrationQueue()
        registrations.initialize(RecordingPlatformBits())

        val itemException = assertThrows(IllegalStateException::class.java) {
            registrations.registerItem(id("late_item"), itemContainer())
        }
        val blockException = assertThrows(IllegalStateException::class.java) {
            registrations.registerBlock(id("late_block"), blockContainer())
        }

        assertEquals("Minecraft module already initialized.", itemException.message)
        assertEquals("Minecraft module already initialized.", blockException.message)
    }
}
