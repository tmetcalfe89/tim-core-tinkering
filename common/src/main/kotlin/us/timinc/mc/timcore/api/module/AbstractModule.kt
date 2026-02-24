package us.timinc.mc.timcore.api.module

import us.timinc.mc.timcore.api.event.TimCoreEvents
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.mod.AbstractMod

/**
 * The basis for a compatibility layer with another mod, mostly for registration. The intent is that modules remain
 * dormant so long as they're unused. Once a mod or feature uses something from a module, the built-in init registers it
 * for waking up with the MODULE_LOAD event.
 *
 * @author Timothy Metcalfe
 */
abstract class AbstractModule<M : AbstractMod<*>>(val mod: M, name: String) {
    /**
     * Each module gets access to its own logger, but they're meant for internal use only.
     */
    internal val logger: Logger = mod.logger.makeSubLogger(listOf("module", name))

    /**
     * Override this to do whatever you need to do when Tim Core wakes up the module.
     */
    internal abstract fun init()

    init {
        logger.sing("Subscribing to MODULE_LOAD.")
        TimCoreEvents.MODULE_LOAD.subscribe {
            logger.sing("Loading.")
            init()
        }
    }
}