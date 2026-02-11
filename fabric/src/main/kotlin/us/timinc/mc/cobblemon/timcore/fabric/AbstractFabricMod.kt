package us.timinc.mc.cobblemon.timcore.fabric

import net.fabricmc.api.ModInitializer
import us.timinc.mc.cobblemon.timcore.AbstractMod

abstract class AbstractFabricMod(val mod: AbstractMod) : ModInitializer {
    override fun onInitialize() {
        mod.init()
    }
}