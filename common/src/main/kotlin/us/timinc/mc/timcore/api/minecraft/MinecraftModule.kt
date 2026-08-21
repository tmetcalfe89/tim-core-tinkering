package us.timinc.mc.timcore.api.minecraft

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.minecraft.world.BlockContainer
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer
import us.timinc.mc.timcore.api.mod.PlatformBits
import us.timinc.mc.timcore.api.module.AbstractModule

/**
 * The Minecraft Tim Core module, offering a convenience layer for various registrations.
 *
 * @author Timothy Metcalfe
 */
object MinecraftModule : AbstractModule<TimCore>(TimCore, "minecraft") {
    private val registrations = MinecraftRegistrationQueue()

    override fun init(platformBits: PlatformBits) {
        registrations.initialize(
            platformBits,
            onRegisteringItems = { count -> logger.sing("Registering $count items.") },
            onRegisteringBlocks = { count -> logger.sing("Registering $count blocks.") },
        )
    }

    /**
     * Register a new item. They all need an ID, but the container holds onto the bits and pieces that make up their
     * game object experience.
     *
     * @throws [IllegalStateException] If the Minecraft module has already initialized.
     * @throws [IllegalStateException] If there's already an item registered with that ID.
     * @see us.timinc.mc.timcore.feature.test.item.TestItem
     */
    fun <T : Item> registerItem(id: ResourceLocation, container: ItemContainer<T>): ItemContainer<T> {
        return registrations.registerItem(id, container) {
            logger.sing("Adding item $id to be registered.")
        }
    }

    fun <T : Block, C : BlockContainer<T>> registerBlock(id: ResourceLocation, container: C): C {
        return registrations.registerBlock(id, container) {
            logger.sing("Adding block $id to be registered.")
        }
    }
}
