package us.timinc.mc.cobblemon.timcore.neoforge.api.mod

import net.neoforged.fml.ModList
import us.timinc.mc.timcore.api.mod.PlatformBits

object NeoForgeBits : PlatformBits() {
    override val platformName: String get() = "NeoForge"
    override fun isModPresent(modId: String) = ModList.get().isLoaded(modId)
}