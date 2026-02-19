package us.timinc.mc.timcore.api.logging

import java.util.*

interface HasLogger {
    val logger: Logger

    fun <R> attachCaseLogger(cb: () -> R): () -> R = {
        withCaseLogger {
            cb()
        }
    }

    fun <P1, R> attachCaseLogger(cb: (P1) -> R): (P1) -> R = { p1: P1 ->
        withCaseLogger {
            cb(p1)
        }
    }

    fun <P1, P2, R> attachCaseLogger(cb: (P1, P2) -> R): (P1, P2) -> R = { p1: P1, p2: P2 ->
        withCaseLogger {
            cb(p1, p2)
        }
    }

    fun <P1, P2, P3, R> attachCaseLogger(cb: (P1, P2, P3) -> R): (P1, P2, P3) -> R = { p1: P1, p2: P2, p3: P3 ->
        withCaseLogger {
            cb(p1, p2, p3)
        }
    }

    fun <P1, P2, P3, P4, R> attachCaseLogger(cb: (P1, P2, P3, P4) -> R): (P1, P2, P3, P4) -> R =
        { p1: P1, p2: P2, p3: P3, p4: P4 ->
            withCaseLogger {
                cb(p1, p2, p3, p4)
            }
        }

    fun <P1, P2, P3, P4, P5, R> attachCaseLogger(cb: (P1, P2, P3, P4, P5) -> R): (P1, P2, P3, P4, P5) -> R =
        { p1: P1, p2: P2, p3: P3, p4: P4, p5: P5 ->
            withCaseLogger {
                cb(p1, p2, p3, p4, p5)
            }
        }

    fun <T> withLogger(
        block: () -> T,
    ): T = LoggerScope.withLogger(logger, block)

    fun <T> withCaseLogger(
        block: () -> T,
    ): T = LoggerScope.withLogger(logger.makeSubLogger(UUID.randomUUID().toString()), block)
}