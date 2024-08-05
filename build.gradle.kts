// Top-level build file where you can add configuration options common to all sub-projects/modules
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    // Secrets Gradle Plugin
    alias(libs.plugins.secrets.gradle.plugin) apply false

    // KSP
    alias(libs.plugins.ksp) apply false

    // Hilt
    alias(libs.plugins.hilt) apply false
}

