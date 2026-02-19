package us.timinc.mc.timcore

import us.timinc.mc.timcore.api.mod.AbstractMod
import us.timinc.mc.timcore.api.mod.ModConfig
import us.timinc.mc.timcore.feature.preventquickballspam.PreventQuickBallSpam

object TimCore : AbstractMod<TimCore.Config>(MOD_ID, Config::class) {
    class Config : ModConfig()

    object Features {
        val preventQuickBallSpam = registerFeature(PreventQuickBallSpam)
    }

    override fun initialize() {
        Features
    }
}