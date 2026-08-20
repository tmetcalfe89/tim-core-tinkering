package us.timinc.mc.timcore.api.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import dev.vishna.watchservice.KWatchChannel
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.mod.AbstractMod
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

/**
 * A generic config file implementation. Attaches itself to the file at the path of `/config/<modId>/<path>.json`,
 * relative to the minecraft root folder. Automatically reads in the values when they're accessed and not yet loaded.
 * Automatically clears the loaded values when the file is changed. Generally speaking, you do not need to create an
 * instance of a config yourself as one is created for each mod and feature automatically.
 *
 * Mods make their own at `/config/<modId>/main.json`. To access the values, I recommend using
 * [AbstractMod.withOperationContext].
 *
 * Features make their own at `/config/<modId>/<featureName>.json`. To access the values, I recommend using
 * [AbstractFeature.withOperationContext].
 *
 * @author Timothy Metcalfe
 */
class Config<T> internal constructor(
    val mod: AbstractMod<*>,
    path: String,
    val clazz: Class<T>,
    val defaultValue: String,
    configRoot: File,
    private val watchChanges: Boolean,
) {
    constructor(
        mod: AbstractMod<*>,
        path: String,
        clazz: Class<T>,
        defaultValue: String = "{}",
    ) : this(mod, path, clazz, defaultValue, File("config"), true)

    private val _values: AtomicReference<T?> = AtomicReference(null)
    private val configFile: File = File(configRoot, "${mod.modId}/$path.json")
    private val lastWriteTime = AtomicLong(0L)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var watcherJob: Job? = null

    companion object {
        private val INVALID_CONFIG_TIMESTAMP: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS")
    }

    /**
     * Gets the config values, loading from file if necessary.
     */
    val values: T
        get() {
            if (_values.get() == null) loadFromFile()
            return _values.get()!!
        }

    private fun getLogger() = Logger("config").makeSubLogger(configFile.path)

    /**
     * Removes the loaded values. Does not immediately trigger a reload, but the next time the values are accessed, will
     * load from file again.
     */
    @Suppress("unused")
    fun reload() {
        LoggerScope.withLogger(getLogger()) {
            val logger = LoggerScope.current()
            logger.sing("Resetting the loaded values to prompt a reload on next access.")
            _values.set(null)
        }
    }

    @Synchronized
    private fun loadFromFile() {
        if (_values.get() != null) return

        LoggerScope.withLogger(getLogger()) {
            val logger = LoggerScope.current()
            logger.sing("Loading config from file.")

            watcherJob?.let {
                it.cancel()
                logger.sing("Shutting down existing file watcher job.")
            }

            val gson = GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create()

            var config = gson.fromJson(defaultValue, clazz)
            configFile.parentFile?.mkdirs()
            var shouldWriteConfig = !configFile.exists()

            if (configFile.exists()) {
                val fileText = runCatching(configFile::readText).getOrElse {
                    logger.alert("Error reading config file; using defaults without modifying the file.")
                    logger.alert(it.stackTraceToString())
                    null
                }

                if (fileText != null) {
                    runCatching {
                        gson.fromJson(fileText, clazz)
                            ?: throw JsonParseException("Config content resolved to null.")
                    }.onSuccess { parsed ->
                        config = parsed
                        shouldWriteConfig = true
                        logger.sing("Loaded config from file.")
                    }.onFailure {
                        logger.alert("Error parsing config file; preserving it before restoring defaults.")
                        logger.alert(it.stackTraceToString())
                        runCatching(::backupInvalidConfig).onSuccess { backup ->
                            logger.warn("Invalid config moved to ${backup.path}.")
                            shouldWriteConfig = true
                        }.onFailure { backupError ->
                            logger.alert("Unable to preserve invalid config; leaving the original file unchanged.")
                            logger.alert(backupError.stackTraceToString())
                        }
                    }
                }
            }

            val json = gson.toJson(config)
            if (shouldWriteConfig) {
                runCatching {
                    configFile.writeText(json)
                    lastWriteTime.set(System.currentTimeMillis())
                    logger.sing("Config file updated.")
                }.onFailure {
                    logger.alert("Error writing config file")
                    logger.alert(it.stackTraceToString())
                }
            }

            _values.set(config)

            if (watchChanges && configFile.exists() && configFile.isFile) {
                val watchChannel = configFile.asWatchChannel(KWatchChannel.Mode.SingleFile)
                watcherJob = scope.launch {
                    logger.sing("Starting watcher.")
                    watchChannel.consumeEach {
                        val now = System.currentTimeMillis()
                        if (now - lastWriteTime.get() > 500) {
                            logger.sing("File updated, invalidating loaded data for reload.")
                            _values.set(null)
                            watchChannel.cancel()
                            this.cancel()
                        }
                    }
                }
            }
        }
    }

    private fun backupInvalidConfig(): File {
        val timestamp = LocalDateTime.now().format(INVALID_CONFIG_TIMESTAMP)
        val extension = configFile.extension.let { if (it.isEmpty()) "" else ".$it" }
        val baseName = configFile.nameWithoutExtension
        var backup = File(configFile.parentFile, "$baseName.invalid-$timestamp$extension")
        var collisionIndex = 1

        while (backup.exists()) {
            backup = File(configFile.parentFile, "$baseName.invalid-$timestamp-$collisionIndex$extension")
            collisionIndex++
        }

        Files.move(configFile.toPath(), backup.toPath())
        return backup
    }
}
