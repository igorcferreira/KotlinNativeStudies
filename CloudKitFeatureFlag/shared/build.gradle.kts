import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
}

kotlin {
    androidLibrary {
        namespace = "dev.igorcferreira.cloudkitfeatureflag.shared"
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // HTTP interface
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)

            // Serialisation
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.contentNegotiation)

            // DI
            implementation(libs.koin.core)
            implementation(libs.koin.annotation)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.koin.compose.navigation)

            // Config
            implementation(projects.arkana)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androix.compose)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// KSP Tasks
dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

// Trigger Common Metadata Generation from Native tasks
tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}
