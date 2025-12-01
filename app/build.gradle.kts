plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)

    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.ksp)
    alias(notation = libs.plugins.hilt)
}

android {
    namespace = "mir.oslav.moaihead"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "mir.oslav.moaihead"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(dependencyNotation = project(":data"))
    implementation(dependencyNotation = project(":firestore"))
    implementation(dependencyNotation = project(":ui"))


    implementation(dependencyNotation = libs.androidx.core.ktx)
    implementation(dependencyNotation = libs.androidx.core.splashscreen)
    implementation(dependencyNotation = libs.androidx.lifecycle.runtime.ktx)
    implementation(dependencyNotation = libs.androidx.activity.compose)
    implementation(dependencyNotation = platform(libs.androidx.compose.bom))
    implementation(dependencyNotation = libs.androidx.compose.ui)
    implementation(dependencyNotation = libs.androidx.compose.ui.graphics)
    implementation(dependencyNotation = libs.androidx.compose.ui.tooling.preview)
    implementation(dependencyNotation = libs.androidx.compose.material3)
    implementation(dependencyNotation = libs.androidx.shapes)


    /** Navigation 3 */
    implementation(dependencyNotation = libs.navigation3.adaptive)
    implementation(dependencyNotation = libs.navigation3.ui)
    implementation(dependencyNotation = libs.navigation3.runtime)
    implementation(dependencyNotation = libs.navigation3.viewmodel)

    implementation(dependencyNotation = libs.serialization.json)


    /** Hilt DI */
    implementation(dependencyNotation = libs.hilt.android)
    ksp(dependencyNotation = libs.hilt.android.compiler)
    implementation(dependencyNotation = libs.hilt.common)
    ksp(dependencyNotation = libs.hilt.compiler)
    implementation(dependencyNotation = libs.hilt.navigation.compose)


    /** WearOS */
    implementation(dependencyNotation = libs.play.services.wearable)
    implementation(dependencyNotation = "androidx.wear:wear:1.3.0")

    testImplementation(dependencyNotation = libs.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.espresso.core)
    androidTestImplementation(dependencyNotation = platform(libs.androidx.compose.bom))
    androidTestImplementation(dependencyNotation = libs.androidx.compose.ui.test.junit4)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.tooling)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}