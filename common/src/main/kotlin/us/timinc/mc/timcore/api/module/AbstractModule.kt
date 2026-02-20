package us.timinc.mc.timcore.api.module

import us.timinc.mc.timcore.api.event.Priority
import us.timinc.mc.timcore.api.event.TimCoreEvents
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.mod.AbstractMod

abstract class AbstractModule<M : AbstractMod<*>>(val mod: M, name: String) {
    val logger: Logger = mod.logger.makeSubLogger(listOf("module", name))

    abstract fun init()

    init {
        logger.sing("Subscribing to MODULE_LOAD.")
        TimCoreEvents.MODULE_LOAD.subscribe(Priority.NORMAL) {
            logger.sing("Loading.")
            init()
        }
    }
}