package us.timinc.mc.timcore.api.config

import com.google.gson.GsonBuilder
import dev.vishna.watchservice.KWatchChannel
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import us.timinc.mc.timcore.api.mod.AbstractMod
import java.io.File
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class Config<T>(
    val mod: AbstractMod<*>,
    path: String,
    val clazz: Class<T>,
    val defaultValue: String = "{}"
) {
    companion object {
        inline fun <reified T> create(mod: AbstractMod<*>, path: String, defaultValue: String = "{}") =
            Config(mod, path, T::class.java, defaultValue)
    }

    private val _values: AtomicReference<T?> = AtomicReference(null)
    private val configFile: File = File("config/${mod.modId}/$path.json")
    private val lastWriteTime = AtomicLong(0L)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var watcherJob: Job? = null

    val values: T
        get() {
            if (_values.get() == null) reload()
            return _values.get()!!
        }

    fun close() {
        scope.cancel()
    }

    fun reload() {
        watcherJob?.cancel()

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
            }.onFailure {
                println("Error reading config file")
                it.printStackTrace()
            }
        }

        val json = gson.toJson(config)
        runCatching {
            configFile.writeText(json)
            lastWriteTime.set(System.currentTimeMillis())
        }.onFailure {
            println("Error writing config file")
            it.printStackTrace()
        }

        _values.set(config)
        
        val watchChannel = configFile.asWatchChannel(KWatchChannel.Mode.SingleFile)
        watcherJob = scope.launch {
            watchChannel.consumeEach {
                val now = System.currentTimeMillis()
                if (now - lastWriteTime.get() > 500) {
                    _values.set(null)
                    watchChannel.cancel()
                    this.cancel()
                }
            }
        }
    }
}