plugins {
    alias(notation = libs.plugins.android.library)
    alias(notation = libs.plugins.kotlin.android)
    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.ksp)

}

android {
    namespace = "moaihead.data"
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

    implementation(dependencyNotation = libs.serialization.json)
    implementation(dependencyNotation = libs.androidx.annotation.jvm)

    /** Hilt DI for dependency injection (https://developer.android.com/training/dependency-injection/hilt-android) */
    implementation(dependencyNotation = libs.hilt.android)
    implementation(dependencyNotation =libs.play.services.tasks)

    /** Mockup data generation (https://github.com/miroslavhybler/ksp-mockup-processor) */
    ksp(dependencyNotation = libs.mockup.annotations)
    ksp(dependencyNotation = libs.mockup.processor)
    implementation(dependencyNotation = libs.mockup.annotations)
    //For mockup, providers are implementing PreviewParameterProvider
    debugImplementation(dependencyNotation = libs.androidx.ui.tooling)
    implementation(dependencyNotation = libs.androidx.ui.tooling.preview)

    testImplementation(dependencyNotation = libs.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.junit)
    androidTestImplementation(dependencyNotation = libs.androidx.espresso.core)
}