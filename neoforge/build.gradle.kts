plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow") version("9.2.2")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()
}

val gametest = sourceSets.create("gametest") {
    kotlin.srcDir(project(":common").file("src/gametest/kotlin"))
    resources.srcDir(project(":common").file("src/gametest/resources"))
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}
configurations.named(gametest.compileClasspathConfigurationName) {
    extendsFrom(configurations[sourceSets.main.get().compileClasspathConfigurationName])
}
configurations.named(gametest.runtimeClasspathConfigurationName) {
    extendsFrom(configurations[sourceSets.main.get().runtimeClasspathConfigurationName])
}
loom.createRemapConfigurations(gametest)
loom.mods.named("main") {
    sourceSet(gametest)
}
loom.runs.create("gameTestServer") {
    environment("gameTestServer")
    forgeTemplate("gameTestServer")
    source(gametest)
    runDir("build/run/gameTest")
    property("neoforge.enabledGameTestNamespaces", "tim_core_gametest")
    ideConfigGenerated(false)
}

val prepareGameTestRun by tasks.registering(Copy::class) {
    from(project(":common").file("src/gametest/config")) {
        into("config")
    }
    from(project(":common").file("src/gametest/resources/data/tim_core_gametest/gametest/structure")) {
        include("*.snbt")
        into("gameteststructures")
    }
    into(layout.buildDirectory.dir("run/gameTest"))
}

repositories {
    mavenCentral()
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net")
}

val shadowBundle = configurations.create("shadowBundle") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:${property("neoforge_version")}")

    modImplementation("com.cobblemon:neoforge:${property("cobblemon_version")}") { isTransitive = false }
    //Needed for cobblemon
    implementation("thedarkcolour:kotlinforforge-neoforge:${property("kotlin_for_forge_version")}") {
        exclude("net.neoforged.fancymodloader", "loader")
    }

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    "developmentNeoForge"("com.github.vishna:watchservice-ktx:master-SNAPSHOT") {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionNeoForge"))

    // Ensure common's external runtime deps are included in the platform shadow
    shadowBundle("com.github.vishna:watchservice-ktx:master-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")
}

tasks.getByName<Test>("test") {
    dependsOn("runGameTestServer")
    useJUnitPlatform()
}

tasks.named<JavaExec>("runGameTestServer") {
    dependsOn(prepareGameTestRun)
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
            "neoforge_version",
            "cobblemon_version",
        ).associateWith(project::property),
    )

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(project.properties)
    }
}

tasks {

    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        exclude("fabric.mod.json")
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        configurations = listOf(shadowBundle)
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }
}
