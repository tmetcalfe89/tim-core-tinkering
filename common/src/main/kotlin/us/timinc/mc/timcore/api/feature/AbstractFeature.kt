package us.timinc.mc.timcore.api.feature

import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.context.OperationContext
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.api.mod.AbstractMod
import kotlin.reflect.KClass

abstract class AbstractFeature<M : AbstractMod<*>, C : FeatureConfig>(
    val mod: M,
    name: String,
    configClass: KClass<C>,
    val requiredMods: List<String> = emptyList(),
) {
    class FeatureOperationContext<C : FeatureConfig>(feature: AbstractFeature<*, C>) : OperationContext<C> {
        override val logger: Logger
            get() = LoggerScope.current()
        override val config: C = feature.config.values
    }

    val config: Config<C> = Config(mod, name, configClass.java)
    val logger: Logger = mod.logger.makeSubLogger(name, { config.values.debugLevel })

    abstract fun initialize()

    fun init() {
        if (!config.values.enabled) return
        if (requiredMods.none(mod.platformBits::isModPresent)) return
        initialize()
    }

    inline fun withOperationContext(action: FeatureOperationContext<C>.() -> Unit) =
        LoggerScope.withLogger(logger.makeCaseLogger(), { with(FeatureOperationContext(this), action) })
}