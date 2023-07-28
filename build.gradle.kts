import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}


var pluginName = project.property("pluginName")!!
group = project.property("group")!!
version = "${project.property("version")!!}-b${gitRevision()}.${gitHash()}"

repositories {
    mavenCentral()
    maven {
        name = "EngineHub"
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "ViaVersion"
        url = uri("https://repo.viaversion.com")
    }
}

dependencies {
    implementation(libs.xseries) { isTransitive = false }
    implementation(libs.fastboard)

    compileOnly(libs.spigot)
    compileOnly(libs.protocollib)
    compileOnly(libs.viavsersion)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}


tasks {

    shadowJar {
        val relocatePackage = "${project.group}.dep."
        archiveFileName.set("${project.property("pluginName")}-${project.property("version")}.jar")
        relocate("com.cryptomorin.xseries", "${relocatePackage}xseries")
        relocate("fr.mrmicky.fastboard", "${relocatePackage}fastboard")

        minimize()
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    jar {
        archiveFileName.set("${pluginName}-${version}.jar")
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

fun gitRevision() : String {
    val out = ByteArrayOutputStream();
    exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = out;
    }
    return out.toString("UTF-8").trim();
}

fun gitHash() : String {
    val out = ByteArrayOutputStream();
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = out;
    }
    return out.toString("UTF-8").trim();
}