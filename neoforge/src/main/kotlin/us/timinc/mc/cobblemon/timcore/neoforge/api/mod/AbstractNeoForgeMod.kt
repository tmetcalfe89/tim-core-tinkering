package us.timinc.mc.cobblemon.timcore.neoforge.api.mod

import us.timinc.mc.cobblemon.timcore.api.mod.AbstractMod

abstract class AbstractNeoForgeMod(val mod: AbstractMod) {
    init {
        mod.init()
    }
}