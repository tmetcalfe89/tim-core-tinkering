package us.timinc.mc.cobblemon.timcore.fabric.api.mod

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer
import us.timinc.mc.timcore.api.mod.PlatformBits

object FabricBits : PlatformBits() {
    override val platformName: String get() = "Fabric"
    override fun isModPresent(modId: String) = FabricLoader.getInstance().isModLoaded(modId)
    override fun registerItems(items: MutableMap<ResourceLocation, ItemContainer<*>>) {
        items.forEach { (resourceLocation, itemContainer) ->
            Registry.register(BuiltInRegistries.ITEM, resourceLocation, itemContainer.item)
            itemContainer.tab?.let { ItemGroupEvents.modifyEntriesEvent(it).register { evt -> evt.accept(itemContainer.item) } }
        }
    }
}