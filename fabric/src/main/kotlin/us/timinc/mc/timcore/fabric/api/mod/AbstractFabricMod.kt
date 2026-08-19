package us.timinc.mc.timcore.fabric.api.mod

import net.fabricmc.api.ModInitializer
import us.timinc.mc.timcore.api.mod.AbstractMod

abstract class AbstractFabricMod(val mod: AbstractMod<*>) : ModInitializer {
    override fun onInitialize() {
        mod.init(FabricBits)
    }
}