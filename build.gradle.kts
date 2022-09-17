import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = project.property("group")!!
version = project.property("version")!!
var pluginName = project.property("pluginName")!!

repositories {
    mavenCentral()
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        ***REMOVED***
        isAllowInsecureProtocol = true
        url = ***REMOVED***
        credentials {
            ***REMOVED***
            ***REMOVED***
        }
    }
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.protocollib)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks {

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
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

