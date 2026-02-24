package us.timinc.mc.timcore.api.mod

import net.minecraft.resources.ResourceLocation
import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.context.OperationContext
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import kotlin.reflect.KClass

abstract class AbstractMod<C : ModConfig>(val modId: String, configClass: KClass<C>) {
    class ModOperationContext<C : ModConfig>(mod: AbstractMod<C>) : OperationContext<C> {
        override val logger: Logger
            get() = LoggerScope.current()
        override val config: C = mod.config.values
    }

    val config: Config<C> = Config(this, "main", configClass.java)
    val logger: Logger = Logger(modId) { config.values.debugLevel }
    lateinit var platformBits: PlatformBits

    @Suppress("unused")
    fun modResource(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

    fun init(platformBits: PlatformBits) {
        logger.sing("Loading for ${platformBits.platformName}.")
        this.platformBits = platformBits
        initialize()
    }

    // Override this in your own mod to do whatever initialization your mods need to do.
    abstract fun initialize()

    override fun toString(): String = modId

    inline fun withOperationContext(action: ModOperationContext<C>.() -> Unit) =
        LoggerScope.withLogger(logger.makeCaseLogger()) { with(ModOperationContext(this), action) }
}