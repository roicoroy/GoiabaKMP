import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "navigation"
            isStatic = true
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.compose.navigation)
            implementation(libs.kotlinx.serialization)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(project(path = ":shared"))
            implementation(project(path = ":di"))
            implementation(project(path = ":feature:home"))
            implementation(project(path = ":feature:profile"))
            implementation(project(path = ":feature:adverts"))
            implementation(project(path = ":feature:adverts:advert-details"))
            implementation(project(path = ":feature:auth:login"))
            implementation(project(path = ":feature:auth:register"))
            implementation(project(path = ":feature:logger"))
            implementation(project(path = ":feature:logger:details"))
        }
    }
}

android {
    namespace = "com.goiaba.navigation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
//        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}