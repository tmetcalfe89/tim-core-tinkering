package us.timinc.mc.timcore.api.minecraft.constants

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab

@Suppress("unused")
object VanillaCreativeTabs {
    val BUILDING_BLOCKS: ResourceKey<CreativeModeTab> = createKey("building_blocks")
    val COLORED_BLOCKS: ResourceKey<CreativeModeTab> = createKey("colored_blocks")
    val NATURAL_BLOCKS: ResourceKey<CreativeModeTab> = createKey("natural_blocks")
    val FUNCTIONAL_BLOCKS: ResourceKey<CreativeModeTab> = createKey("functional_blocks")
    val REDSTONE_BLOCKS: ResourceKey<CreativeModeTab> = createKey("redstone_blocks")
    val TOOLS_AND_UTILITIES: ResourceKey<CreativeModeTab> = createKey("tools_and_utilities")
    val COMBAT: ResourceKey<CreativeModeTab> = createKey("combat")
    val FOOD_AND_DRINKS: ResourceKey<CreativeModeTab> = createKey("food_and_drinks")
    val INGREDIENTS: ResourceKey<CreativeModeTab> = createKey("ingredients")

    private fun createKey(name: String): ResourceKey<CreativeModeTab> =
        ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.withDefaultNamespace(name)
        )
}