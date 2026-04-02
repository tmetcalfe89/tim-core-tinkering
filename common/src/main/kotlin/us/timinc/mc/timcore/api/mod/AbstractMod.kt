package us.timinc.mc.timcore.api.mod

import net.minecraft.resources.ResourceLocation
import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.context.OperationContext
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import kotlin.reflect.KClass

/**
 * # Mods
 * The main starting point for any Tim Core-based mod. Register any [AbstractFeature] by referencing them in its
 * [initialize] method.
 *
 * ## Operation Context
 * In order to easily get access to a snapshot of the current relevant bits for a given execution of a workflow, use
 * [withOperationContext] and surround your workflow as a lambda.
 *
 * ### Config
 * Mods have their own config that has the properties seen in [ModConfig] at the least. Extend this class in your own
 * implementation of mod, pass it through, and the [Config] will be automatically managed.
 *
 * ### Logger
 * Mods have their own [Logger], with the mod's ID. This means their prefix is:
 *
 * `[<mod id>]`
 *
 * @author Timothy Metcalfe
 */
abstract class AbstractMod<C : ModConfig>(val modId: String, configClass: KClass<C>) {
    class ModOperationContext<C : ModConfig>(mod: AbstractMod<C>) : OperationContext<C> {
        override val logger: Logger = mod.logger.makeCaseLogger()
        override val config: C = mod.config.values
    }

    /**
     * Mods have their own config that has the properties seen in [ModConfig] at the least. Extend this class in your own
     * implementation of mod, pass it through, and the [Config] will be automatically managed.
     *
     * You most likely want to use [withOperationContext] instead of accessing this directly.
     */
    val config: Config<C> = Config(this, "main", configClass.java)

    /**
     * Mods have their own [Logger], with the mod's ID. This means their prefix is:
     *
     * `[<mod id>]`
     *
     * You most likely want to use [withOperationContext] instead of accessing this directly.
     */
    val logger: Logger = Logger(modId) { config.values.debugLevel }
    lateinit var platformBits: PlatformBits

    /**
     * A convenience method for getting a [ResourceLocation] with this mod's namespace and the given path.
     */
    @Suppress("unused")
    fun modResource(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

    /**
     * Used by the platform implementation to kick this mod up. Don't use it yourself.
     */
    fun init(platformBits: PlatformBits) {
        logger.sing("Loading for ${platformBits.platformName}.")
        this.platformBits = platformBits
        initialize(platformBits)
    }

    /**
     * Override this to do your own setup.
     */
    abstract fun initialize(platformBits: PlatformBits)

    override fun toString(): String = modId

    /**
     * Runs a block with a [ModOperationContext], giving direct access to its properties and registering its logger
     * as the logger for the workflow.
     */
    inline fun withOperationContext(action: ModOperationContext<C>.() -> Unit) =
        ModOperationContext(this).let { context ->
            LoggerScope.withLogger(context.logger) {
                with(context, action)
            }
        }
}