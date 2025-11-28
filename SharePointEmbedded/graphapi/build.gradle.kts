import com.rickclephas.kmp.nativecoroutines.gradle.ExposedSeverity
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.nativecoroutines)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.kmmdeploy)
    alias(libs.plugins.mokkery)
    `maven-publish`
}

group = "dev.igorcferreira"
version = "1.0"

kotlin {
    jvmToolchain(21)
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "MSGraphAPI"
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0"
        summary = "MSGraphAPI module"
        name = "MSGraphAPI"
        authors = "Igor Ferreira"
        license = "MIT"
        homepage = "https://github.com/igorcferreira/KotlinNativeStudies/tree/main/SharePointEmbedded"
        ios.deploymentTarget = "26"

        version = project.version.toString()

        framework {
            baseName = "MSGraphAPI"
            isStatic = true
        }

        pod("MSAL/native-auth")
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }

        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.androidx.activity.compose)
            implementation(libs.microsoft.identity.client.msal)
        }
        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutines.test)
            implementation(libs.ktor.client.mock)
        }
        androidUnitTest.dependencies {
            implementation(libs.androidx.testExt.junit)
            implementation(libs.androidx.test.core)
            implementation(libs.mockk)
            implementation(libs.mockk.android)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.espresso.core)
            implementation(libs.mockk)
            implementation(libs.mockk.android)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

nativeCoroutines {
    exposedSeverity = ExposedSeverity.NONE
}

android {
    namespace = "dev.igorcferreira.msgraphapi"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    useLibrary("android.test.mock")
    useLibrary("android.test.runner")
}
