package us.timinc.mc.timcore.api.logging

object LoggerScope {
    val stackTL = ThreadLocal<ArrayDeque<Logger>>()

    fun currentOrNull(): Logger? =
        stackTL.get()?.lastOrNull()

    fun current(): Logger =
        currentOrNull() ?: Logger("fallback")

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
