import org.gradle.internal.os.OperatingSystem
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.9.21"
    id("org.jetbrains.compose") version "1.5.12"
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

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation("org.slf4j:slf4j-api:2.0.12")

    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation(kotlin("test"))
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

    test {
        useJUnitPlatform()
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

        from("${layout.buildDirectory.get()}/compose/binaries/main-release/app/KotaTime/")
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
                "java.net.http",
                "jdk.unsupported"
            )

            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Dmg,
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