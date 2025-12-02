plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

   // alias(notation = libs.plugins.ksp)
   // alias(notation = libs.plugins.hilt)
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
        versionName = "1.0.0-DEV"

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    useLibrary("wear-sdk")

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
    implementation(project(":data"))
    implementation(project(":ui"))


    implementation(libs.play.services.wearable)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.wear.compose.material3)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.shapes)

    implementation(libs.wear.navigation)

    /** Hilt DI */
  //  implementation(dependencyNotation = libs.hilt.android)
   // ksp(dependencyNotation = libs.hilt.android.compiler)
  //  implementation(dependencyNotation = libs.hilt.common)
 //   ksp(dependencyNotation = libs.dagger.compiler)
   // implementation(dependencyNotation = libs.hilt.navigation.compose)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}