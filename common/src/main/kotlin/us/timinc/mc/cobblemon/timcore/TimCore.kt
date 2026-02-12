package us.timinc.mc.cobblemon.timcore

import us.timinc.mc.cobblemon.timcore.api.config.SimpleJsonConfig
import us.timinc.mc.cobblemon.timcore.api.mod.AbstractMod

object TimCore : AbstractMod(MOD_ID) {
    class MainConfig {
        val option: String = "howdy"
    }

    object Configs {
        val mainConfig = registerConfig(SimpleJsonConfig.create<MainConfig>(this@TimCore, MAIN_CONFIG_NAME))
    }

    override fun initialize() {
        Configs
    }
}