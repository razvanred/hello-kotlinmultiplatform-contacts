plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "red.razvan.hello.kotlinmultiplatform.contacts"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "red.razvan.hello.kotlinmultiplatform.contacts"
        versionCode = 1
        versionName = "1.0.0"

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures.viewBinding = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.tools.desugar)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.google.android.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.common.java8)

    implementation(projects.repository)

    implementation(libs.koin.android)
}