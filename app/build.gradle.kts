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
        versionName = "1.0.0-DEV"

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

    flavorDimensions("storage")
    productFlavors {
        create("firestore") {
            dimension = "storage"
            buildConfigField("String", "DATA_SOURCE", "\"firestore\"")
        }
        create("room") {
            isDefault = true
            dimension = "storage"
            buildConfigField("String", "DATA_SOURCE", "\"local\"")
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
        buildConfig = true
    }
}

dependencies {
    implementation(dependencyNotation = project(":data"))
    implementation(dependencyNotation = project(":ui"))
    // Flavor-specific dependency
    "firestoreImplementation"(dependencyNotation = project(":data-firestore"))
    implementation(dependencyNotation = project(":data-room"))

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


    /** Navigation 3 (https://developer.android.com/guide/navigation/navigation-3) */
    implementation(dependencyNotation = libs.navigation3.adaptive)
    implementation(dependencyNotation = libs.navigation3.ui)
    implementation(dependencyNotation = libs.navigation3.runtime)
    implementation(dependencyNotation = libs.navigation3.viewmodel)

    implementation(dependencyNotation = libs.serialization.json)


    /** Hilt DI for dependency injection (https://developer.android.com/training/dependency-injection/hilt-android) */
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


gradle.startParameter.taskNames.find { it.contains("firestore", ignoreCase = true) }?.let {
    val googleServicesFile = file("google-services.json")
    if (!googleServicesFile.exists()) {
        throw GradleException(
            "google-services.json not found in ${project.projectDir}. " +
                    "For the 'firestore' flavor, this file is required. " +
                    "Please follow the setup instructions in the README to configure your own Firebase project and place the file in the correct location ('app/google-services.json')."
        )
    }
}