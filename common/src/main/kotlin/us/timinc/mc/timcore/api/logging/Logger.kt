package us.timinc.mc.timcore.api.logging

import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.logging.Logger.LogLevel.NONE
import us.timinc.mc.timcore.api.mod.AbstractMod
import java.util.*

/**
 * A simple logger that can have stacked prefixes. [AbstractMod] and [AbstractFeature] both make their own instances of
 * this for quick instance, check them out, you probably want to use the operation context in one of those.
 *
 * @author Timothy Metcalfe
 */
@Suppress("unused")
class Logger(
    val path: List<String>,
    val getLevel: () -> LogLevel = { LogLevel.SING },
) {
    constructor(path: String, getLevel: () -> LogLevel = { LogLevel.SING }) : this(listOf(path), getLevel)

    val joinedPath = path.joinToString("") { "[$it]" }

    enum class LogLevel(val level: Int, val prefix: String) {
        /**
         * Make your mod sing! This one's for the lowest-priority logs to let users know what your mod is doing.
         */
        SING(4, "🎶"),

        /**
         * Warn users about oddities happening in your mod. Usually due to bad input or awkward situations they should
         * probably know about.
         */
        WARN(3, "⚠️"),

        /**
         * Toss big issues in users' faces. They can't blame you if you put it here, their fault they put it on [NONE].
         */
        ALERT(2, "⚡"),

        /**
         * This one's for the config, so they can turn all your logs off.
         */
        NONE(1, "")
    }

    /**
     * Make a sub-logger at the given path, possibly with a new, overriding way to get the level.
     */
    fun makeSubLogger(
        subPath: List<String>,
        getLevel: (() -> LogLevel)? = null
    ): Logger =
        Logger(path + subPath, getLevel ?: this.getLevel)

    /**
     * Convenience function for the [makeSubLogger] that uses the list.
     */
    fun makeSubLogger(
        subPath: String,
        getLevel: (() -> LogLevel)? = null
    ): Logger = makeSubLogger(listOf(subPath), getLevel)

    /**
     * Makes a sub-logger with a UUID for a specific case of one of your workflows.
     */
    fun makeCaseLogger() = makeSubLogger(UUID.randomUUID().toString())

    private fun log(logLevel: LogLevel, msg: String) {
        if (logLevel == LogLevel.NONE)
            throw IllegalArgumentException("Log level cannot be NONE when logging. This is for config only.")
        if (logLevel.level > getLevel().level) return
        println("$joinedPath: ${logLevel.prefix} $msg")
    }

    /**
     * Make your mod sing! This one's for the lowest-priority logs to let users know what your mod is doing.
     */
    fun sing(msg: String) = log(LogLevel.SING, msg)

    /**
     * Warn users about oddities happening in your mod. Usually due to bad input or awkward situations they should
     * probably know about.
     */
    fun warn(msg: String) = log(LogLevel.WARN, msg)

    /**
     * Toss big issues in users' faces. They can't blame you if you put it here, their fault they put it on [NONE].
     */
    fun alert(msg: String) = log(LogLevel.ALERT, msg)
}