package us.timinc.mc.timcore.api.mod

abstract class PlatformBits {
    abstract val platformName: String
    abstract fun isModPresent(modId: String): Boolean
}