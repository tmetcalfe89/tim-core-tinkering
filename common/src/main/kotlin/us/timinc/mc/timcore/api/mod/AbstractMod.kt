package us.timinc.mc.timcore.api.mod

import net.minecraft.resources.ResourceLocation
import us.timinc.mc.timcore.api.config.Config
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.feature.FeatureConfig
import us.timinc.mc.timcore.api.logging.Logger
import kotlin.reflect.KClass

abstract class AbstractMod<C : ModConfig>(val modId: String, configClass: KClass<C>) {
    val config: Config<C> = Config(this, "main", configClass.java)
    val logger: Logger = Logger(modId) { config.values.debugLevel }
    private val features: MutableList<AbstractFeature<*, *>> = mutableListOf()
    lateinit var platformBits: PlatformBits

    fun <C : FeatureConfig, M: AbstractMod<*>, F : AbstractFeature<M, C>> registerFeature(
        feature: F
    ): F {
        features += feature
        return feature
    }

    fun initFeatures() {
        for (feature in features) {
            feature.init()
        }
    }

    fun modResource(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

    fun init(platformBits: PlatformBits) {
        this.platformBits = platformBits
        initialize()
        initFeatures()
    }

    abstract fun initialize()
}