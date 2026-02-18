package us.timinc.mc.cobblemon.timcore.fabric.api.mod

import net.fabricmc.loader.api.FabricLoader
import us.timinc.mc.timcore.api.mod.PlatformBits

object FabricBits : PlatformBits() {
    override fun isModPresent(modId: String) = FabricLoader.getInstance().isModLoaded(modId)
}