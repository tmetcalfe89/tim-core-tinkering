package us.timinc.mc.timcore.api.logging

import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.logging.LoggerScope.current
import us.timinc.mc.timcore.api.mod.AbstractMod


/**
 * A cockamamie idea I had to use scope to ensure that even downstream calls in a workflow have access to the current
 * logger. In your own code, call [current] to get access to either the current logger for the current workflow, or a
 * fallback logger. This pattern attempts to continue to reference logs as part of a case logger, for example, without
 * having to pass the case logger down as a parameter.
 *
 * React's `useContext` sends its regards.
 *
 * @author Timothy Metcalfe
 */
object LoggerScope {
    val stackTL = ThreadLocal<ArrayDeque<Logger>>()

    /**
     * Get the current logger or default to null.
     */
    fun currentOrNull(): Logger? =
        stackTL.get()?.lastOrNull()

    /**
     * Get the current logger or default to a (new) fallback logger.
     */
    fun current(): Logger =
        currentOrNull() ?: Logger("fallback")

    /**
     * Attach a logger to the current workflow. Just pass your logger and the workflow as a lambda. You shouldn't need
     * to do this manually, as [AbstractMod.withOperationContext] and [AbstractFeature.withOperationContext] do the same
     * and give you a case logger (a logger with a unique ID for a specific instance of a specific workflow).
     */
    inline fun withLogger(
        logger: Logger,
        block: () -> Unit,
    ) {
        val stack = stackTL.get() ?: ArrayDeque<Logger>().also(stackTL::set)
        stack.addLast(logger)
        return try {
            block()
        } catch (t: Throwable) {
            throw t
        } finally {
            stack.removeLast()
            if (stack.isEmpty()) stackTL.remove()
        }
    }
}
