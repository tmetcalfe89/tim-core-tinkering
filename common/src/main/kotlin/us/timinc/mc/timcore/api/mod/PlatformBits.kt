package us.timinc.mc.timcore.api.mod

import net.minecraft.resources.ResourceLocation
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer

/**
 * A representation of the platform this mod is currently running on (NeoForge and Fabric supported out of the box).
 * Common code should never use platform-specific code. The goal here is to allow platform implementations to extend
 * this and provide answers as to how to do platform-specific things.
 */
abstract class PlatformBits {
    /**
     * The name of the platform.
     */
    abstract val platformName: String

    /**
     * Get whether a mod is present by its mod ID.
     */
    abstract fun isModPresent(modId: String): Boolean

    /**
     * Register a collection of items.
     */
    abstract fun registerItems(items: MutableMap<ResourceLocation, ItemContainer<*>>)
}