import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

group = project.property("group")!!
version = project.property("version")!!
var pluginName = project.property("pluginName")!!

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
}

dependencies {
    implementation(libs.xseries) { isTransitive = false }
    implementation(libs.fastboard)

    compileOnly(libs.spigot)
    compileOnly(libs.protocollib)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}


tasks {

    shadowJar {
        archiveFileName.set("${project.property("pluginName")}-${project.property("version")}.jar")
        relocate("com.cryptomorin.xseries", "${project.group}.dep.xseries")
        relocate("fr.mrmicky.fastboard", "${project.group}.dep.fastboard")

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

