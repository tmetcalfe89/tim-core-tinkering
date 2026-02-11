package us.timinc.mc.cobblemon.timcore.neoforge

import us.timinc.mc.cobblemon.timcore.AbstractMod

abstract class AbstractNeoForgeMod(val mod: AbstractMod) {
    init {
        mod.init()
    }
}