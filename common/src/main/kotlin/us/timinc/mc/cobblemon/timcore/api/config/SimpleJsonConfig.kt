package us.timinc.mc.cobblemon.timcore.api.config

import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class SimpleJsonConfig<T>(
    override val modId: String,
    val path: String,
    val clazz: Class<T>,
    val defaultValue: String = "{}"
) : Config<T> {
    private var _values: T? = null
    override val values: T
        get() {
            if (_values == null) reload()
            return _values!!
        }

    override fun reload() {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .create()

        var config = gson.fromJson(defaultValue, clazz)
        val configFile = File("config/$modId/$path.json")
        configFile.parentFile.mkdirs()

        if (configFile.exists()) {
            try {
                val fileReader = FileReader(configFile)
                config = gson.fromJson(fileReader, clazz)
                fileReader.close()
            } catch (e: Exception) {
                println("Error reading config file")
                e.printStackTrace()
            }
        }

        val pw = PrintWriter(configFile)
        gson.toJson(config, pw)
        pw.close()

        _values = config
    }
}