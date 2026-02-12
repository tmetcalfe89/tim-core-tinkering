package us.timinc.mc.cobblemon.timcore.api.config

import us.timinc.mc.cobblemon.timcore.api.mod.AbstractMod

interface Config<T> {
    val mod: AbstractMod
    val values: T

    fun reload()
}