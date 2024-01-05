import org.gradle.internal.os.OperatingSystem
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.9.21"
    id("org.jetbrains.compose") version "1.5.11"
}

group = "me.gamercoder215.kotatime"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    google()

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs) {
        exclude("org.jetbrains.compose.material")
    }
    implementation("com.bybutter.compose:compose-jetbrains-expui-theme:2.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

val os = System.getProperty("os.name").lowercase().split(" ")[0]
val jdk = JavaVersion.VERSION_17

java {
    sourceCompatibility = jdk
    targetCompatibility = jdk
}

tasks {
    build {
        dependsOn("release")
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = jdk.toString()
        }
    }

    processResources {
        include(".env")
        include("assets/**/*")
    }

    register("release", Zip::class) {
        dependsOn("createReleaseDistributable")

        from("${layout.buildDirectory.get()}/compose/binaries/main-release/app/")
        archiveFileName.set("KotaTime-$version-$os.zip")
    }
}

compose.desktop {
    application {
        mainClass = "me.gamercoder215.kotatime.MainKt"
        buildTypes.release.proguard {
            isEnabled = false
        }

        nativeDistributions {
            modules(
                "java.instrument",
                "java.management",
                "java.naming",
                "java.net.http",
                "java.security.jgss",
                "java.sql",
                "jdk.unsupported"
            )

            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Pkg,
                TargetFormat.Deb
            )

            description = "A desktop app for WakaTime"
            copyright = "Copyright (c) GamerCoder 2024-Latest. All rights reserved."
            vendor = "GamerCoder"
            licenseFile.set(project.file("LICENSE"))

            windows {
                iconFile.set(file("src/main/resources/assets/icon/icon128.ico"))
            }

            macOS {
                iconFile.set(file("src/main/resources/assets/icon/icon128.icns"))
            }

            linux {
                iconFile.set(file("src/main/resources/assets/icon/icon128.png"))
            }
        }
    }
}