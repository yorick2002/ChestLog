import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "2.2.21"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "yorick.gg"
version = "1.0"

// Correct Kotlin DSL jar configuration
tasks.jar {
    archiveBaseName.set("ChestLog")
}

tasks.shadowJar {
    archiveBaseName.set("ChestLog")
    archiveClassifier.set("")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

// run-paper configuration
tasks.runServer {
    minecraftVersion("1.21")
}

// Copy the built jar into your plugins folder
val copyJar by tasks.registering(Copy::class) {
    from("$buildDir/libs")
    include("*.jar")
    into("D:/Development/Languages/Java/Minecraft/Server/plugins")
}

// Ensure shadowJar runs before build, and copyJar runs after build
tasks.named("build") {
    dependsOn("shadowJar")
    finalizedBy(copyJar)
}

// Java / Kotlin toolchain
kotlin {
    jvmToolchain(21)
}

// Expand variables in plugin.yml
tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
