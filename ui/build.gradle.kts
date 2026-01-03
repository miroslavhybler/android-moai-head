plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.kotlin.android)
    alias(notation = libs.plugins.kotlin.compose)
    alias(notation = libs.plugins.ksp)
}

android {
    namespace = "moaihead.ui"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(dependencyNotation = libs.androidx.core.ktx)
    implementation(dependencyNotation = platform(libs.androidx.compose.bom))
    implementation(dependencyNotation = libs.androidx.compose.ui)
    implementation(dependencyNotation = libs.androidx.compose.ui.graphics)
    implementation(dependencyNotation = libs.androidx.compose.ui.tooling.preview)
    implementation(dependencyNotation = libs.androidx.compose.material3)
    implementation(dependencyNotation = libs.androidx.shapes)
    implementation(dependencyNotation = libs.androidx.lifecycle.viewmodel.ktx)


    /** Hilt DI for dependency injection (https://developer.android.com/training/dependency-injection/hilt-android) */
    implementation(dependencyNotation = libs.hilt.android)
    ksp(dependencyNotation = libs.hilt.android.compiler)
    implementation(dependencyNotation = libs.hilt.common)
    ksp(dependencyNotation = libs.hilt.compiler)


    testImplementation(dependencyNotation = libs.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.espresso.core)
}