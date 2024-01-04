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
    implementation(compose.desktop.currentOs)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("io.javalin:javalin:5.6.3")
    implementation("org.slf4j:slf4j-simple:2.0.10")
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

        nativeDistributions {
            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Pkg,
                TargetFormat.Deb
            )

            packageName = "KotaTime"
            packageVersion = version.toString()

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