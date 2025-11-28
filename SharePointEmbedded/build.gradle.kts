import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinCocoapods) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kmmdeploy) apply false
    alias(libs.plugins.mokkery) apply false
}

val bundleInstall by tasks.registering(Exec::class) {
    workingDir = file("./")
    commandLine = mutableListOf("bash", "-c", "bundle install")
    standardOutput = System.out
    errorOutput = System.err
}

val arkana by tasks.registering(Exec::class) {
    dependsOn(bundleInstall)

    workingDir = file("./")
    commandLine = mutableListOf("bash", "-c", "bundle exec arkana -l kotlin")
    standardOutput = System.out
    errorOutput = System.err
}

tasks.prepareKotlinBuildScriptModel.dependsOn(arkana)
