package us.timinc.mc.timcore.api.config

import com.google.gson.GsonBuilder
import dev.vishna.watchservice.KWatchChannel
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import us.timinc.mc.timcore.api.logging.Logger
import us.timinc.mc.timcore.api.logging.LoggerScope
import us.timinc.mc.timcore.api.feature.AbstractFeature
import us.timinc.mc.timcore.api.mod.AbstractMod
import java.io.File
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
class Config<T>(
    val mod: AbstractMod<*>,
    path: String,
    val clazz: Class<T>,
    val defaultValue: String = "{}"
) {
    private val _values: AtomicReference<T?> = AtomicReference(null)
    private val configFile: File = File("config/${mod.modId}/$path.json")
    private val lastWriteTime = AtomicLong(0L)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var watcherJob: Job? = null

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

    private fun loadFromFile() {
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

            if (configFile.exists()) {
                runCatching {
                    val text = configFile.readText()
                    gson.fromJson(text, clazz)
                }.onSuccess { parsed ->
                    if (parsed != null) config = parsed
                    logger.sing("Loaded config from file.")
                }.onFailure {
                    logger.alert("Error reading config file.")
                    it.printStackTrace()
                }
            }

            val json = gson.toJson(config)
            runCatching {
                configFile.writeText(json)
                lastWriteTime.set(System.currentTimeMillis())
                logger.sing("Config file updated.")
            }.onFailure {
                logger.alert("Error writing config file")
                logger.alert(it.stackTraceToString())
            }

            _values.set(config)

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