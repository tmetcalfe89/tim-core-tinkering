package us.timinc.mc.timcore.api.minecraft

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer
import us.timinc.mc.timcore.api.mod.PlatformBits
import us.timinc.mc.timcore.api.module.AbstractModule

/**
 * The Minecraft Tim Core module, offering a convenience layer for various registrations.
 *
 * @author Timothy Metcalfe
 */
object MinecraftModule : AbstractModule<TimCore>(TimCore, "minecraft") {
    private val items: MutableMap<ResourceLocation, ItemContainer<*>> = mutableMapOf()
    private var initialized: Boolean = false

    override fun init(platformBits: PlatformBits) {
        if (items.isNotEmpty()) {
            logger.sing("Registering ${items.size} items.")
            platformBits.registerItems(items)
        }
        initialized = true
    }

    /**
     * Register a new item. They all need an ID, but the container holds onto the bits and pieces that make up their
     * game object experience.
     *
     * @throws [IllegalStateException] If the Minecraft module has already initialized.
     * @throws [IllegalStateException] If there's already an item registered with that ID.
     * @see us.timinc.mc.timcore.feature.test.item.TestItem
     */
    fun <T : Item> registerItem(id: ResourceLocation, container: ItemContainer<T>) : ItemContainer<T> {
        if (initialized) throw IllegalStateException("Minecraft module already initialized.")
        if (items.containsKey(id)) throw IllegalStateException("Item $id already registered.")

        logger.sing("Adding item $id to be registered.")
        items[id] = container
        return container
    }
}