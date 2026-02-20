package us.timinc.mc.timcore.api.mod

import net.minecraft.resources.ResourceLocation
import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.logging.Logger
import kotlin.reflect.KClass

abstract class AbstractMod<C : ModConfig>(val modId: String, configClass: KClass<C>) {
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
}