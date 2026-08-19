package us.timinc.mc.timcore.neoforge.api.mod

import us.timinc.mc.timcore.api.mod.AbstractMod

abstract class AbstractNeoForgeMod(mod: AbstractMod<*>) {
    init {
        mod.init(NeoForgeBits)
    }
}