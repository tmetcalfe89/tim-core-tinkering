package us.timinc.mc.cobblemon.timcore.neoforge.api.mod

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModList
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer
import us.timinc.mc.timcore.api.mod.PlatformBits

object NeoForgeBits : PlatformBits() {
    override val platformName: String get() = "NeoForge"
    override fun isModPresent(modId: String) = ModList.get().isLoaded(modId)
    override fun registerItems(items: MutableMap<ResourceLocation, ItemContainer<*>>) {
        if (items.isNotEmpty()) {
            MOD_BUS.addListener { e: RegisterEvent ->
                if (e.registry != BuiltInRegistries.ITEM) return@addListener
                items.forEach { (id: ResourceLocation, itemContainer: ItemContainer<*>) ->
                    e.register(Registries.ITEM, id) { itemContainer.item }
                }
            }
            MOD_BUS.addListener { e: BuildCreativeModeTabContentsEvent ->
                items.values.filter { it.tab == e.tabKey }.forEach { e.accept(it.item) }
            }
        }
    }
}