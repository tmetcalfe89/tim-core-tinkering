package us.timinc.mc.timcore

import us.timinc.mc.timcore.api.event.TimCoreEvents
import us.timinc.mc.timcore.api.mod.AbstractMod
import us.timinc.mc.timcore.api.mod.ModConfig
import us.timinc.mc.timcore.api.mod.PlatformBits
import us.timinc.mc.timcore.feature.cobblemon.customdroplogic.CustomDropLogic
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.PreventQuickBallSpam
import us.timinc.mc.timcore.feature.test.block.TestBlock
import us.timinc.mc.timcore.feature.test.item.TestItem

object TimCore : AbstractMod<TimCore.Config>(MOD_ID, Config::class) {
    class Config : ModConfig()

    override fun initialize(platformBits: PlatformBits) {
        // Reference your features in the initialization of your mod to wake them up.
        PreventQuickBallSpam
        CustomDropLogic
        TestItem
        TestBlock

        // Don't worry about this, it's for Tim Core specifically.
        timCoreSpecificInit(platformBits)
    }

    // DO NOT copy this to your mod. It's the thing that tells everybody else to wake up.
    fun timCoreSpecificInit(platformBits: PlatformBits) {
        logger.sing("Waking up the features.")
        TimCoreEvents.FEATURE_LOAD.fire(Unit)
        logger.sing("Waking up the modules.")
        TimCoreEvents.MODULE_LOAD.fire(platformBits)
    }
}