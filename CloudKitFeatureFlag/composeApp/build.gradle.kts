import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "dev.igorcferreira.cloudkitfeatureflag"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.igorcferreira.cloudkitfeatureflag"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    dependencies {
        implementation(libs.compose.uiToolingPreview)
        implementation(libs.androidx.activity.compose)
        implementation(libs.compose.runtime)
        implementation(libs.compose.foundation)
        implementation(libs.compose.material3)
        implementation(libs.compose.ui)
        implementation(libs.compose.components.resources)
        implementation(libs.compose.uiToolingPreview)
        implementation(libs.androidx.lifecycle.viewmodelCompose)
        implementation(libs.androidx.lifecycle.runtimeCompose)
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.navigation3.ui)
        implementation(libs.androidx.navigation3.runtime)
        implementation(libs.androidx.material3.adaptive.navigation3)
        implementation(libs.androidx.lifecycle.viewmodel.navigation3)
        implementation(libs.koin.core)
        implementation(libs.koin.android)
        implementation(projects.shared)
        testImplementation(libs.kotlin.test)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    debugImplementation(libs.compose.uiTooling)
}

