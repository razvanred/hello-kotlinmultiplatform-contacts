import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm()
    iosArm64()
    iosSimulatorArm64()

    // If you want to use the https://github.com/benasher44/uuid library
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.uuid)
            }
        }
    }

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            isStatic = true
            baseName = "RepositoryKt"
        }
    }
}