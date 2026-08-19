package us.timinc.mc.timcore.neoforge.api.mod

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModList
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import us.timinc.mc.timcore.api.minecraft.world.BlockContainer
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

    override fun registerBlocks(blocks: MutableMap<ResourceLocation, BlockContainer<*>>) {
        if (blocks.isNotEmpty()) {
            MOD_BUS.addListener { e: RegisterEvent ->
                if (e.registry != BuiltInRegistries.BLOCK) return@addListener
                blocks.forEach { (id: ResourceLocation, blockContainer: BlockContainer<*>) ->
                    e.register(Registries.BLOCK, id) { blockContainer.block }
                }
            }
            MOD_BUS.addListener { e: RegisterEvent ->
                if (e.registry != BuiltInRegistries.ITEM) return@addListener
                blocks.forEach { (id, blockContainer) ->
                    blockContainer.item?.let { blockItem -> e.register(Registries.ITEM, id) { blockItem } }
                }
            }
            MOD_BUS.addListener { e: RegisterEvent ->
                if (e.registry != BuiltInRegistries.BLOCK_ENTITY_TYPE) return@addListener
                blocks.forEach { (id, blockContainer) ->
                    if (blockContainer !is BlockContainer.WithEntity<*, *>) return@forEach
                    e.register(Registries.BLOCK_ENTITY_TYPE, id) { blockContainer.blockEntityType }
                }
            }
            MOD_BUS.addListener { e: BuildCreativeModeTabContentsEvent ->
                blocks.values.filter { it.tab == e.tabKey }.forEach { blockContainer ->
                    blockContainer.item?.let(e::accept)
                }
            }
        }
    }
}