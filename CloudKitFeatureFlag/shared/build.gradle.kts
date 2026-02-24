import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
}

val frameworkName = project.property("project.framework-name") as String
val frameworkVersion = project.property("project.version-code") as String
val frameworkVersionString = project.property("project.version-name") as String

kotlin {
    val applicationId = project.property("project.application-id") as String

    androidLibrary {
        namespace = "dev.igorcferreira.cloudkitfeatureflag"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        aarMetadata {
            minAgpVersion = libs.versions.agp.get()
            minCompileSdk = libs.versions.android.compileSdk.get().toInt()
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTest {
            targetSdk {
                version = release(libs.versions.android.compileSdk.get().toInt())
            }
            this.applicationId =  applicationId
        }
    }

    val xcf = XCFramework(frameworkName)

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = frameworkName
            isStatic = true
            binaryOption("bundleId", applicationId)
            binaryOption("bundleVersion", frameworkVersion)
            binaryOption("bundleShortVersionString", frameworkVersionString)
            xcf.add(this)
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

            //File
            implementation(libs.squareup.okio)
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

val debugPackage = tasks.register<PackageFramework>("packageDebugFramework") {
    dependsOn("assemble${frameworkName}DebugXCFramework")
    outputDir = rootProject.layout.projectDirectory.file("Package/debug")
    framework = project.layout.buildDirectory.file("XCFrameworks/debug/$frameworkName.xcframework")
    packageTemplate = rootProject.layout.projectDirectory.file("Package.swift.template")
}

val releasePackage = tasks.register<PackageFramework>("packageReleaseFramework") {
    dependsOn("assemble${frameworkName}ReleaseXCFramework")
    outputDir = rootProject.layout.projectDirectory.file("Package/release")
    framework = project.layout.buildDirectory.file("XCFrameworks/release/$frameworkName.xcframework")
    packageTemplate = rootProject.layout.projectDirectory.file("Package.swift.template")
}

tasks.register("packageFramework") {
    dependsOn("assemble${frameworkName}XCFramework", debugPackage, releasePackage)
}
