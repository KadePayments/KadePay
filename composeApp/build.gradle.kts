import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.ktlintGradle)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidxRoom)
}

val currentOs: String = System.getProperty("os.name").lowercase()

ktlint {
    filter {
        exclude("**/generated/**")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    jvmToolchain(21)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    jvm()
    @OptIn(ExperimentalWasmDsl::class)
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.room.sqlite.wrapper)
            implementation(libs.secp256k1.kmp.jni.android)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.com.google.crypto.tink)
        }
        androidUnitTest.dependencies {
            implementation(libs.robolectric)
            implementation(libs.compose.test.junit4)
            implementation(libs.compose.test.manifest)
            implementation(libs.secp256k1.kmp.jni.jvm)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.navigation)

            implementation(libs.bitcoin.kmp)
            implementation(libs.secp256k1.kmp)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.test.coroutines)
            implementation(libs.compose.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.secp256k1.kmp.jni.jvm)
        }
        jvmTest.dependencies {
            val targetDep =
                when {
                    currentOs.contains("linux") -> libs.secp256k1.kmp.jni.jvm.linux
                    currentOs.contains("windows") -> libs.secp256k1.kmp.jni.jvm.windows
                    currentOs.contains("mac") || currentOs.contains("darwin") -> libs.secp256k1.kmp.jni.jvm.macos
                    else -> error("Unsupported OS: $currentOs")
                }
            implementation(targetDep)
        }
    }
}

android {
    namespace = "com.kade.pay"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.kade.pay"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    add("kspJvm", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)

    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.kade.pay.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Exe,
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.AppImage,
            )
            packageName = "com.kade.pay"
            packageVersion = "1.0.0"
        }
    }
}
