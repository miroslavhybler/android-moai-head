plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.kotlin.android)
    alias(notation = libs.plugins.ksp)

}

android {
    namespace = "moaihead.room"
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
    ksp {
        arg(k = "room.schemaLocation", v = "${projectDir.path}/room-schemas")
    }
}

dependencies {

    implementation(dependencyNotation = project(":data"))

    /** Room Database for local data storage (https://developer.android.com/training/data-storage/room) */
    implementation(dependencyNotation = libs.room.runtime)
    implementation(dependencyNotation = libs.room.ktx)
    ksp(dependencyNotation = libs.room.compiler)


    /** Hilt DI for dependency injection (https://developer.android.com/training/dependency-injection/hilt-android) */
    implementation(dependencyNotation = libs.hilt.android)
    ksp(dependencyNotation = libs.hilt.android.compiler)
    implementation(dependencyNotation = libs.hilt.common)
    ksp(dependencyNotation = libs.hilt.compiler)


    testImplementation(dependencyNotation = libs.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.espresso.core)
}