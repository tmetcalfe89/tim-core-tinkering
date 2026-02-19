package us.timinc.mc.timcore.api.config

interface HasConfig<T : Any> {
    val config: Config<T>
}