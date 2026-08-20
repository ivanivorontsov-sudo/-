plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.ivan.calculator"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.ivan.calculator"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

kotlin { jvmToolchain(17) }
