package us.timinc.mc.timcore.feature.test.item

import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.minecraft.MinecraftModule
import us.timinc.mc.timcore.api.minecraft.constants.VanillaCreativeTabs
import us.timinc.mc.timcore.api.minecraft.world.ItemContainer

object TestItem : AbstractFeature<TimCore, TestItem.Config>(
    TimCore,
    "test/item",
    Config::class,
) {
    class Config : FeatureConfig(
        enabled = false,
        debugLevel = Logger.LogLevel.SING,
    )

    object ModItems {
        @Suppress("unused")
        val BASIC_TEST_ITEM =
            MinecraftModule.registerItem(
                mod.modResource("basic_test_item"),
                ItemContainer.Basic()
            )
        @Suppress("unused")
        val CREATIVE_TAB_TEST_ITEM =
            MinecraftModule.registerItem(
                mod.modResource("creative_tab_test_item"),
                ItemContainer.Basic(tab = VanillaCreativeTabs.FOOD_AND_DRINKS)
            )
    }

    override fun initialize() {
        ModItems
    }
}