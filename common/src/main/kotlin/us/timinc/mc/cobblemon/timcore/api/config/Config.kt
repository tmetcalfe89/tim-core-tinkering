package us.timinc.mc.cobblemon.timcore.api.config

interface Config<T> {
    val modId: String
    val values: T

    fun reload()
}