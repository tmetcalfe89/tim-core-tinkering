package us.timinc.mc.timcore.feature.cobblemon.customdroplogic

import com.cobblemon.mod.common.api.drop.DropEntry
import us.timinc.mc.timcore.TimCore
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.feature.cobblemon.customdroplogic.dropentry.TagItemDropEntry

object CustomDropLogic : AbstractFeature<TimCore, CustomDropLogic.Config>(
    TimCore,
    "custom_drop_logic",
    Config::class,
    setOf("cobblemon"),
) {
    class Config : FeatureConfig()

    override fun initialize() {
        logger.sing("Registering item tag drop entry type.")
        DropEntry.register("item_tag", TagItemDropEntry::class.java)
    }
}