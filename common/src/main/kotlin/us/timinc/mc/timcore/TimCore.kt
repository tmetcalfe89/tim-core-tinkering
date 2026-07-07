package us.timinc.mc.timcore

import us.timinc.mc.timcore.api.event.OneTimeEvent
import us.timinc.mc.timcore.api.mod.AbstractMod
import us.timinc.mc.timcore.api.mod.ModConfig
import us.timinc.mc.timcore.api.mod.PlatformBits
import us.timinc.mc.timcore.feature.cobblemon.customdroplogic.CustomDropLogic
import us.timinc.mc.timcore.feature.cobblemon.preventquickballspam.PreventQuickBallSpam
import us.timinc.mc.timcore.feature.test.block.TestBlock
import us.timinc.mc.timcore.feature.test.blockentity.TestBlockEntity
import us.timinc.mc.timcore.feature.test.item.TestItem

object TimCore : AbstractMod<TimCore.Config>(MOD_ID, Config::class) {
    // Don't worry about these two when making your mod, this is underlying logic.
    private val moduleLoad = OneTimeEvent<PlatformBits>()
    private val featureLoad = OneTimeEvent<Unit>()

    class Config : ModConfig()

    override fun initialize(platformBits: PlatformBits) {
        // Reference your features in the initialization of your mod to wake them up.
        PreventQuickBallSpam
        CustomDropLogic
        TestItem
        TestBlock
        TestBlockEntity

        // Don't worry about this, it's for Tim Core specifically.
        timCoreSpecificInit(platformBits)
    }

    // DO NOT copy this to your mod. It's the thing that tells everybody else to wake up.
    private fun timCoreSpecificInit(platformBits: PlatformBits) {
        logger.sing("Waking up the features.")
        featureLoad.fire(Unit)
        logger.sing("Waking up the modules.")
        moduleLoad.fire(platformBits)
    }

    // Don't worry about these two when making your mod, this is underlying logic.
    internal fun getModuleLoad() = moduleLoad.asSubscribable()
    internal fun getFeatureLoad() = featureLoad.asSubscribable()
}
