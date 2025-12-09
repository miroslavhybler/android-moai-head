plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(notation = libs.plugins.ksp)
    alias(notation = libs.plugins.hilt)
}

android {
    namespace = "moaihead.firestore"
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
}

dependencies {

    implementation(dependencyNotation =project(":data"))
    implementation(dependencyNotation = project(":data-room"))

    implementation(dependencyNotation =libs.androidx.core.ktx)

    /** Firebase */
    implementation(dependencyNotation = platform(libs.firebase.bom))
    implementation(dependencyNotation =libs.firebase.firestore)
    implementation(dependencyNotation =libs.firebase.auth)


    /** Hilt DI for dependency injection (https://developer.android.com/training/dependency-injection/hilt-android) */
    implementation(dependencyNotation = libs.hilt.android)
    ksp(dependencyNotation = libs.hilt.android.compiler)
    implementation(dependencyNotation = libs.hilt.common)
    ksp(dependencyNotation = libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}