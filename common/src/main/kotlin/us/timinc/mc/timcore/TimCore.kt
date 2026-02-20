package us.timinc.mc.timcore

import us.timinc.mc.timcore.api.event.TimCoreEvents
import us.timinc.mc.timcore.api.mod.AbstractMod
import us.timinc.mc.timcore.api.mod.ModConfig
import us.timinc.mc.timcore.feature.preventquickballspam.PreventQuickBallSpam

object TimCore : AbstractMod<TimCore.Config>(MOD_ID, Config::class) {
    class Config : ModConfig()

    override fun initialize() {
        // Reference your features in the initialization of your mod to wake them up.
        PreventQuickBallSpam

        // Don't worry about this, it's for Tim Core specifically.
        timCoreSpecificInit()
    }

    // DO NOT copy this to your mod. It's the thing that tells everybody else to wake up.
    fun timCoreSpecificInit() {
        logger.sing("Waking up the features.")
        TimCoreEvents.FEATURE_LOAD.fire(Unit)
        logger.sing("Waking up the modules.")
        TimCoreEvents.MODULE_LOAD.fire(Unit)
    }
}