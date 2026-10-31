import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}


var pluginName = project.property("pluginName").toString()
var javaVersion = 25

group = project.property("group").toString()
version = "${project.property("version")}-b${gitRevision()}.${gitHash()}"


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "ViaVersion"
        url = uri("https://repo.viaversion.com")
    }
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.get())

    implementation(libs.xseries) { isTransitive = false }
    implementation(libs.fastboard)

    compileOnly(libs.protocollib)
    compileOnly(libs.viaversion)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.junit.jupiter)
}


tasks.shadowJar {
    archiveFileName.set("${project.property("pluginName")}-${project.property("version")}.jar")

    fun relocate(pkg: String) = relocate(pkg, "${project.group}.dependency.$pkg");
    relocate("com.cryptomorin.xseries")
    relocate("fr.mrmicky.fastboard")

    minimize()
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.release = javaVersion
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filter<ReplaceTokens>(
            "beginToken" to "\${",
            "endToken" to "}",
            "tokens" to mapOf(
                "name" to pluginName,
                "version" to version
            )
        )
    }

}

fun gitRevision(): String {
    return try {
        providers.exec {
            commandLine("git", "rev-list", "--count", "HEAD")
        }.standardOutput.asText.get().trim()
    } catch (e: Exception) {
        "0"
    }
}

fun gitHash(): String {
    return try {
        providers.exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
        }.standardOutput.asText.get().trim()
    } catch (e: Exception) {
        "unknown"
    }
}