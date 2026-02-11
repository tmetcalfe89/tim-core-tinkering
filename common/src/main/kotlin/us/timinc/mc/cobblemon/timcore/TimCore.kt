package us.timinc.mc.cobblemon.timcore

import us.timinc.mc.cobblemon.timcore.api.config.SimpleJsonConfig

object TimCore : AbstractMod("tim_core") {
    class MainConfig {
        val option: String = "howdy"
    }

    object Configs {
        val mainConfig = registerConfig(SimpleJsonConfig(modId, "main", MainConfig::class.java))
    }

    override fun initialize() {
        Configs
    }
}