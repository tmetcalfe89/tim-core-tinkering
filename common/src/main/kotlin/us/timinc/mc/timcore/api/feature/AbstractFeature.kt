package us.timinc.mc.timcore.api.feature

import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.context.OperationContext
import us.timinc.mc.timcore.api.event.TimCoreEvents
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.api.mod.AbstractMod
import kotlin.reflect.KClass

/**
 * # Features
 * A scoped chunk of functionality added by a mod, that conceptually acts as a child to the mod. Users should be able to
 * voluntarily disable features for their server/modpack from your mod, and this is handled by the enabled property in
 * their property. Features can also require mods be loaded by their id, and if they aren't they will automatically
 * disable themselves.
 *
 * ## Operation Context
 * In order to easily get access to a snapshot of the current relevant bits for a given execution of a workflow, use
 * [withOperationContext] and surround your workflow as a lambda.
 *
 * ### Config
 * Features have their own config that has the properties seen in [FeatureConfig] at the least. Extend this class in
 * your own implementation of feature, pass it through, and the [Config] will be automatically managed.
 *
 * ### Logger
 * Features have their own [Logger], made as a sub-logger of their mod's logger with the feature's name. This means
 * their prefix is:
 *
 * `[<mod id>][<feature name>]`
 *
 * @author Timothy Metcalfe
 */
abstract class AbstractFeature<M : AbstractMod<*>, C : FeatureConfig>(
    val mod: M,
    name: String,
    configClass: KClass<C>,
    val requiredMods: Set<String> = emptySet(),
) {
    class FeatureOperationContext<C : FeatureConfig>(feature: AbstractFeature<*, C>) : OperationContext<C> {
        override val logger: Logger = feature.logger.makeCaseLogger()
        override val config: C = feature.config.values
    }

    /**
     * Features have their own config that has the properties seen in [FeatureConfig] at the least. Extend this class in
     * your own implementation of feature, pass it through, and the [Config] will be automatically managed.
     *
     * You most likely want to use [withOperationContext] instead of accessing this directly.
     */
    val config: Config<C> = Config(mod, name, configClass.java)

    /**
     * Features have their own [Logger], made as a sub-logger of their mod's logger with the feature's name. This means
     * their prefix is:
     *
     * `[<mod id>][<feature name>]`
     *
     * You most likely want to use [withOperationContext] instead of accessing this directly.
     */
    val logger: Logger = mod.logger.makeSubLogger(listOf("feature", name)) { config.values.debugLevel }

    /**
     * Override this to do your own setup.
     */
    abstract fun initialize()

    private fun init() {
        if (!config.values.enabled) {
            logger.warn("Not loading feature: Config has feature disabled.")
            return
        }
        if (!requiredMods.all(mod.platformBits::isModPresent)) {
            logger.warn("Not loading feature")
            return
        }
        logger.sing("Loading feature.")
        initialize()
    }

    /**
     * Runs a block with a [FeatureOperationContext], giving direct access to its properties and registering its logger
     * as the logger for the workflow.
     */
    inline fun withOperationContext(action: FeatureOperationContext<C>.() -> Unit) =
        FeatureOperationContext(this).let { context ->
            LoggerScope.withLogger(context.logger) {
                with(context, action)
            }
        }

    init {
        TimCoreEvents.FEATURE_LOAD.subscribe { init() }
    }
}