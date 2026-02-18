package us.timinc.mc.timcore.api.mod

abstract class PlatformBits {
    abstract fun isModPresent(modId: String): Boolean
}