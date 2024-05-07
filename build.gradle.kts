import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version ("8.1.1")
    id("io.papermc.paperweight.userdev") version "1.7.0"
}


var pluginName = project.property("pluginName")!!
group = project.property("group")!!
version = "${project.property("version")!!}-b${gitRevision()}.${gitHash()}"

repositories {
    mavenCentral()
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
    paperweight.paperDevBundle(libs.versions.paper)

    implementation(libs.xseries) { isTransitive = false }
    implementation(libs.fastboard)

    api(libs.bundles.kyori.adventure);

    compileOnly(libs.protocollib)
    compileOnly(libs.viavsersion)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}


tasks {

    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        archiveFileName.set("${project.property("pluginName")}-${project.property("version")}.jar")

        fun reloc(pkg: String) = relocate(pkg, "${project.group}.dependency.$pkg");
        reloc("com.cryptomorin.xseries")
        reloc("fr.mrmicky.fastboard")

        minimize()
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