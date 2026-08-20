package us.timinc.mc.timcore.api.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import us.timinc.mc.timcore.api.mod.AbstractMod
import us.timinc.mc.timcore.api.mod.ModConfig
import us.timinc.mc.timcore.api.mod.PlatformBits
import java.io.File
import java.nio.file.Files

class ConfigTest {
    private class TestModConfig : ModConfig()
    private class TestValues {
        val message: String = "default"
    }

    private fun testMod(modId: String) =
        object : AbstractMod<TestModConfig>(modId, TestModConfig::class) {
            override fun initialize(platformBits: PlatformBits) = Unit
        }

    @Test
    fun `malformed config is preserved before defaults are restored`() {
        val configRoot = Files.createTempDirectory("tim-core-config-test").toFile()
        try {
            val mod = testMod("malformed_config")
            val configDirectory = File(configRoot, mod.modId).apply { mkdirs() }
            val configFile = File(configDirectory, "main.json").apply { writeText("{ invalid") }
            val config = Config(mod, "main", TestValues::class.java, "{}", configRoot, false)

            assertEquals("default", config.values.message)
            assertTrue(configFile.isFile)

            val backups = configDirectory.listFiles { file ->
                file.name.startsWith("main.invalid-") && file.extension == "json"
            }.orEmpty()
            assertEquals(1, backups.size)
            assertEquals("{ invalid", backups.single().readText())
        } finally {
            configRoot.deleteRecursively()
        }
    }

    @Test
    fun `read failure does not replace the existing path`() {
        val configRoot = Files.createTempDirectory("tim-core-config-test").toFile()
        try {
            val mod = testMod("unreadable_config")
            val configPath = File(configRoot, "${mod.modId}/main.json").apply { mkdirs() }
            val config = Config(mod, "main", TestValues::class.java, "{}", configRoot, false)

            assertEquals("default", config.values.message)
            assertTrue(configPath.isDirectory)
        } finally {
            configRoot.deleteRecursively()
        }
    }
}
