package us.timinc.mc.cobblemon.timcore.api.mod

import us.timinc.mc.cobblemon.timcore.api.config.Config

abstract class AbstractMod(val modId: String) {
    val configs: MutableList<Config<*>> = mutableListOf()

    fun <ConfigType> registerConfig(config: Config<ConfigType>): Config<ConfigType> {
        configs += config
        return config
    }

    fun reloadConfigs() {
        for (config in configs) {
            config.reload()
        }
    }

    fun init() {
        initialize()
        reloadConfigs()
    }

    abstract fun initialize()
}