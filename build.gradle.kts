import groovy.json.JsonSlurper
import java.util.zip.ZipFile

plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version("2.2.21")

    id("dev.architectury.loom") version("1.11-SNAPSHOT") apply false
    id("architectury-plugin") version("3.4-SNAPSHOT") apply false
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = project.property("modMyVersion")!!
    group = project.property("maven_group")!!

    repositories {
        mavenCentral()
        maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        maven("https://maven.impactdev.net/repository/development/")
        maven("https://maven.neoforged.net/releases")
        maven("https://thedarkcolour.github.io/KotlinForForge/")
        maven("https://api.modrinth.com/maven")
        maven("https://jitpack.io")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    java {
        withSourcesJar()
    }
}

// The root project only coordinates the platform subprojects. Its archives
// contain no mod code or metadata and can be mistaken for distributable mods.
tasks.named("jar") {
    enabled = false
}

tasks.named("sourcesJar") {
    enabled = false
}

val fabricModJar = project(":fabric").layout.buildDirectory.file(
    "libs/${property("archives_base_name")}-fabric-${project.version}.jar",
)
val neoForgeModJar = project(":neoforge").layout.buildDirectory.file(
    "libs/${property("archives_base_name")}-neoforge-${project.version}.jar",
)

val verifyModMetadata by tasks.registering {
    group = "verification"
    description = "Verifies the metadata packaged in the distributable mod JARs."

    dependsOn(":fabric:remapJar", ":neoforge:remapJar")
    inputs.files(fabricModJar, neoForgeModJar)
    inputs.property("expectedVersion", project.property("modMyVersion"))
    inputs.property("expectedFabricKotlinVersion", project.property("fabric_kotlin"))

    doLast {
        val expectedModId = "tim_core"
        val expectedVersion = project.property("modMyVersion").toString()
        val expectedIssues = "https://github.com/timinc-mcmods/tim-core/issues"
        val expectedSource = "https://github.com/timinc-mcmods/tim-core"
        val expectedIcon = "tim_core_small.png"

        fun requireMetadata(condition: Boolean, message: String) {
            if (!condition) {
                throw GradleException(message)
            }
        }

        ZipFile(fabricModJar.get().asFile).use { jar ->
            val metadataEntry = jar.getEntry("fabric.mod.json")
                ?: throw GradleException("Fabric JAR does not contain fabric.mod.json")
            val metadata = jar.getInputStream(metadataEntry).bufferedReader().use { reader ->
                @Suppress("UNCHECKED_CAST")
                JsonSlurper().parse(reader) as Map<String, Any?>
            }

            @Suppress("UNCHECKED_CAST")
            val contact = metadata["contact"] as? Map<String, Any?>
                ?: throw GradleException("Fabric metadata does not contain contact information")
            @Suppress("UNCHECKED_CAST")
            val entrypoints = metadata["entrypoints"] as? Map<String, Any?>
                ?: throw GradleException("Fabric metadata does not contain entrypoints")
            @Suppress("UNCHECKED_CAST")
            val mainEntrypoints = entrypoints["main"] as? List<Map<String, Any?>>
                ?: throw GradleException("Fabric metadata does not contain a main entrypoint")
            @Suppress("UNCHECKED_CAST")
            val dependencies = metadata["depends"] as? Map<String, Any?>
                ?: throw GradleException("Fabric metadata does not contain dependencies")

            requireMetadata(metadata["id"] == expectedModId, "Fabric metadata has the wrong mod ID")
            requireMetadata(
                metadata["version"] == "$expectedVersion-fabric",
                "Fabric metadata has the wrong version",
            )
            requireMetadata(contact["issues"] == expectedIssues, "Fabric metadata has the wrong issues URL")
            requireMetadata(contact["sources"] == expectedSource, "Fabric metadata has the wrong source URL")
            requireMetadata(metadata["icon"] == expectedIcon, "Fabric metadata declares the wrong icon")
            requireMetadata(jar.getEntry(expectedIcon) != null, "Fabric JAR does not contain its declared icon")
            requireMetadata(
                mainEntrypoints.any { it["adapter"] == "kotlin" },
                "Fabric metadata does not use the Kotlin entrypoint adapter",
            )
            requireMetadata(
                dependencies["fabric-language-kotlin"] == ">=${project.property("fabric_kotlin")}",
                "Fabric metadata does not declare the configured Fabric Language Kotlin dependency",
            )
        }

        ZipFile(neoForgeModJar.get().asFile).use { jar ->
            val metadataEntry = jar.getEntry("META-INF/neoforge.mods.toml")
                ?: throw GradleException("NeoForge JAR does not contain META-INF/neoforge.mods.toml")
            val metadata = jar.getInputStream(metadataEntry).bufferedReader().use { it.readText() }

            fun hasAssignment(key: String, value: String): Boolean =
                Regex("(?m)^\\s*${Regex.escape(key)}\\s*=\\s*\"${Regex.escape(value)}\"\\s*$")
                    .containsMatchIn(metadata)

            requireMetadata(hasAssignment("modId", expectedModId), "NeoForge metadata has the wrong mod ID")
            requireMetadata(
                hasAssignment("version", "$expectedVersion-neoforge"),
                "NeoForge metadata has the wrong version",
            )
            requireMetadata(
                hasAssignment("issueTrackerURL", expectedIssues),
                "NeoForge metadata has the wrong issues URL",
            )
            requireMetadata(hasAssignment("modUrl", expectedSource), "NeoForge metadata has the wrong source URL")
            requireMetadata(hasAssignment("logoFile", expectedIcon), "NeoForge metadata declares the wrong icon")
            requireMetadata(jar.getEntry(expectedIcon) != null, "NeoForge JAR does not contain its declared icon")
        }
    }
}

tasks.named("check") {
    dependsOn(verifyModMetadata)
}

