plugins {
    alias(notation = libs.plugins.android.application)
    alias(notation = libs.plugins.kotlin.android)
    alias(notation = libs.plugins.kotlin.compose)
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    kotlin {
        jvmToolchain(jdkVersion = 17)
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(dependencyNotation = project(":ui"))
    implementation(dependencyNotation = project(":data"))
    //Room used for storing data temporarily when phone is not available
    implementation(dependencyNotation = project(":data-room"))



    implementation(dependencyNotation = platform(libs.androidx.compose.bom))
    implementation(dependencyNotation = libs.androidx.compose.ui)
    implementation(dependencyNotation = libs.androidx.compose.ui.graphics)
    implementation(dependencyNotation = libs.androidx.compose.ui.tooling.preview)
    implementation(dependencyNotation = libs.wear.compose.material3)
    implementation(dependencyNotation = libs.androidx.compose.material3)
    implementation(dependencyNotation = libs.androidx.compose.foundation)
    implementation(dependencyNotation = libs.androidx.wear.tooling.preview)
    implementation(dependencyNotation = libs.androidx.activity.compose)
    implementation(dependencyNotation = libs.androidx.core.splashscreen)
    implementation(dependencyNotation = libs.androidx.shapes)
    implementation(dependencyNotation = libs.androidx.lifecycle.runtime.ktx)

    implementation(dependencyNotation = libs.wear.navigation)

    /** WearOS Tiles (https://developer.android.com/codelabs/wear-tiles) */
    implementation(dependencyNotation = libs.wear.tiles)
    debugImplementation(dependencyNotation = libs.wear.tiles.tooling)
    debugImplementation(dependencyNotation = libs.wear.tiles.tooling.preview)
    implementation(dependencyNotation = libs.horologist.compose)
    implementation(dependencyNotation = libs.horologist.tiles)
    implementation(dependencyNotation = libs.play.services.wearable)
    implementation(dependencyNotation = libs.wear.protolayout)
    implementation(dependencyNotation = libs.wear.protolayout.expression)
    implementation(dependencyNotation = libs.wear.protolayout.material3)

    implementation(dependencyNotation = libs.guava)
    implementation(dependencyNotation = libs.serialization.json)

    /** Hilt DI */
    implementation(dependencyNotation = libs.hilt.android)
    ksp(dependencyNotation = libs.hilt.android.compiler)
    implementation(dependencyNotation = libs.hilt.common)
    ksp(dependencyNotation = libs.dagger.compiler)
    implementation(dependencyNotation = libs.hilt.navigation.compose)

    androidTestImplementation(dependencyNotation = platform(libs.androidx.compose.bom))
    androidTestImplementation(dependencyNotation = libs.androidx.compose.ui.test.junit4)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.tooling)
    debugImplementation(dependencyNotation = libs.androidx.compose.ui.test.manifest)
}