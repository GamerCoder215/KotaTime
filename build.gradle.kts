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
}

compose.desktop {
    application {
        mainClass = "me.gamercoder215.kotatime.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Pkg,
                TargetFormat.Deb,
                TargetFormat.AppImage
            )
            packageName = "KotaTime"
            packageVersion = version.toString()
        }
    }
}