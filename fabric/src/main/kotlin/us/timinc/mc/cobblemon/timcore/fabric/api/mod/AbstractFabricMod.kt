package us.timinc.mc.cobblemon.timcore.fabric.api.mod

import net.fabricmc.api.ModInitializer
import us.timinc.mc.cobblemon.timcore.api.mod.AbstractMod

abstract class AbstractFabricMod(val mod: AbstractMod) : ModInitializer {
    override fun onInitialize() {
        mod.init()
    }
}