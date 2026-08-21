plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow") version("9.2.2")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()

    mixin {
        defaultRefmapName.set("mixins.${project.name}.refmap.json")
    }
}

fabricApi.configureTests {
    createSourceSet.set(true)
    modId.set("tim_core_gametest")
    enableClientGameTests.set(false)
}

sourceSets.named("gametest") {
    kotlin.srcDir(project(":common").file("src/gametest/kotlin"))
    resources.srcDir(project(":common").file("src/gametest/resources"))
}

val prepareGameTestConfig by tasks.registering(Copy::class) {
    from(project(":common").file("src/gametest/config"))
    into(layout.buildDirectory.dir("run/gameTest/config"))
}

// Cobblemon's Showdown bootstrap needs ICU on both the GameTest process classpath
// and the test mod's Fabric classpath group. Keep it isolated from published artifacts.
val gametestRuntimeLibraries = configurations.create("gametestRuntimeLibraries")
gametestRuntimeLibraries.isCanBeConsumed = false
gametestRuntimeLibraries.isCanBeResolved = true
configurations.named("gametestRuntimeOnly") {
    extendsFrom(gametestRuntimeLibraries)
}

val shadowCommon = configurations.create("shadowCommon")
shadowCommon.isCanBeConsumed = false
shadowCommon.isCanBeResolved = true

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    modImplementation(fabricApi.module("fabric-command-api-v2", property("fabric_api_version").toString()))
    modImplementation(fabricApi.module("fabric-item-group-api-v1", property("fabric_api_version").toString()))

    //needed for cobblemon
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin")}")
    modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}") { isTransitive = false }

    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))
    shadowCommon(project(":common", configuration = "transformProductionFabric"))

    "developmentFabric"("com.github.vishna:watchservice-ktx:master-SNAPSHOT") {
        isTransitive = false
    }

    // Ensure common's external runtime deps are included in the platform shadow
    shadowCommon("com.github.vishna:watchservice-ktx:master-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")
    "modGametestImplementation"(fabricApi.module("fabric-gametest-api-v1", property("fabric_api_version").toString()))
    gametestRuntimeLibraries("com.ibm.icu:icu4j:73.2")

    runtimeOnly("org.graalvm.sdk:graal-sdk:${property("graal_version")}")
    runtimeOnly("org.graalvm.truffle:truffle-api:${property("graal_version")}")
    runtimeOnly("org.graalvm.js:js:${property("graal_version")}")
}

loom.mods.named("tim_core_gametest") {
    configuration(gametestRuntimeLibraries)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JavaExec>("runGameTest") {
    dependsOn(prepareGameTestConfig)
    classpath(gametestRuntimeLibraries)
}

tasks.processResources {
    inputs.properties(
        listOf(
            "modId",
            "modMyVersion",
            "modName",
            "modDescription",
            "modAuthor",
            "modIssues",
            "modSource",
            "modDiscord",
            "minecraft_version",
            "fabric_kotlin",
            "cobblemon_version",
        ).associateWith(project::property),
    )

    filesMatching("fabric.mod.json") {
        expand(project.properties)
    }

}

tasks {

    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        configurations = listOf(shadowCommon)
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }
}
