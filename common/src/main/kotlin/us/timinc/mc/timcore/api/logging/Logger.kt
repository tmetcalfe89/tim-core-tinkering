package us.timinc.mc.timcore.api.logging

@Suppress("unused")
class Logger(
    val path: List<String>,
    val getLevel: () -> LogLevel = { LogLevel.SING },
) {
    constructor(path: String, getLevel: () -> LogLevel = { LogLevel.SING }) : this(listOf(path), getLevel)

    enum class LogLevel(val level: Int, val prefix: String) {
        SING(4, "🎶"),
        WARN(3, "⚠️"),
        ALERT(2, "⚡"),
        NONE(1, "")
    }

    fun makeSubLogger(
        subPath: List<String>,
        getLevel: (() -> LogLevel)? = null
    ): Logger =
        Logger(path + subPath, getLevel ?: this.getLevel)

    fun makeSubLogger(
        subPath: String,
        getLevel: (() -> LogLevel)? = null
    ): Logger = makeSubLogger(listOf(subPath), getLevel)

    fun log(logLevel: LogLevel, msg: String) {
        if (logLevel.level > getLevel().level) return
        println("${path.joinToString("") { "[$it]" }}: ${logLevel.prefix} $msg")
    }

    fun sing(msg: String) = log(LogLevel.SING, msg)

    fun warn(msg: String) = log(LogLevel.WARN, msg)

    fun alert(msg: String) = log(LogLevel.ALERT, msg)
}