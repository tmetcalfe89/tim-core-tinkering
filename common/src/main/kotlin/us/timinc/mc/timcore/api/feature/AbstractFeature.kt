package us.timinc.mc.timcore.api.feature

import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.config.HasConfig
import us.timinc.mc.timcore.api.logging.HasLogger
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.api.mod.AbstractMod
import kotlin.reflect.KClass

abstract class AbstractFeature<M : AbstractMod<*>, C : FeatureConfig>(
    val mod: M,
    name: String,
    configClass: KClass<C>,
    val requiredMods: List<String> = emptyList(),
) : HasLogger, HasConfig<C> {
    override val config: Config<C> = Config(mod, name, configClass.java)
    override val logger: Logger = mod.logger.makeSubLogger(name, { config.values.debugLevel })

    abstract fun initialize()

    fun init() {
        if (!config.values.enabled) return
        if (requiredMods.none(mod.platformBits::isModPresent)) return
        initialize()
    }

    fun <R> withFeature(cb: (C, Logger) -> R): () -> R {
        return {
            withCaseLogger {
                val caseLogger = LoggerScope.current()
                cb(config.values, caseLogger)
            }
        }
    }

    fun <P1, R> withFeature(cb: (P1, C, Logger) -> R): (P1) -> R {
        return { p1: P1 ->
            withCaseLogger {
                val caseLogger = LoggerScope.current()
                cb(p1, config.values, caseLogger)
            }
        }
    }

    fun <P1, P2, R> withFeature(cb: (P1, P2, C, Logger) -> R): (P1, P2) -> R {
        return { p1: P1, p2: P2 ->
            withCaseLogger {
                val caseLogger = LoggerScope.current()
                cb(p1, p2, config.values, caseLogger)
            }
        }
    }

    fun <P1, P2, P3, R> withFeature(cb: (P1, P2, P3, C, Logger) -> R): (P1, P2, P3) -> R {
        return { p1: P1, p2: P2, p3: P3 ->
            withCaseLogger {
                val caseLogger = LoggerScope.current()
                cb(p1, p2, p3, config.values, caseLogger)
            }
        }
    }

    fun <P1, P2, P3, P4, R> withFeature(cb: (P1, P2, P3, P4, C, Logger) -> R): (P1, P2, P3, P4) -> R {
        return { p1: P1, p2: P2, p3: P3, p4: P4 ->
            withCaseLogger {
                val caseLogger = LoggerScope.current()
                cb(p1, p2, p3, p4, config.values, caseLogger)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, R> withFeature(cb: (P1, P2, P3, P4, P5, C, Logger) -> R): (P1, P2, P3, P4, P5) -> R {
        return { p1: P1, p2: P2, p3: P3, p4: P4, p5: P5 ->
            withCaseLogger {
                val caseLogger = LoggerScope.current()
                cb(p1, p2, p3, p4, p5, config.values, caseLogger)
            }
        }
    }
}