plugins {
    val kotlinVersion: String by System.getProperties()

    java
    kotlin("jvm") version kotlinVersion
    id("fabric-loom") version "0.12.+"
}

group = "xyz.horizr.onetimeoverrides"
version = "1.0.0"

repositories {
    mavenCentral()
}

val kotlinVersion: String by System.getProperties()
val fabricLoaderVersion = "0.14.9"
val fabricLanguageKotlinVersion = "1.8.2+kotlin.$kotlinVersion"

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.4:v2")
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")

    include("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to project.version,
                    "fabricLoaderVersion" to fabricLoaderVersion,
                    "fabricLanguageKotlinVersion" to fabricLanguageKotlinVersion
                )
            )
        }
    }

    remapJar {
        addNestedDependencies.set(true)
    }
}

loom {
    runtimeOnlyLog4j.set(true)
}
