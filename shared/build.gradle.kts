import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.buildKonfig)
    kotlin("plugin.serialization").version("2.3.0")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()


    cocoapods {
        version = "1.0.0"
        homepage = "xgamma.in"
        summary = "Compose module setup for cocoapods"
        ios.deploymentTarget = "26.0"
        framework {
            baseName = "SharedFramework"
        }
        pod("GoogleSignIn")

        podfile = project.file("../iosApp/Podfile")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.alwinsden.dino.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

private object KeyStoreValues {
    const val CLIENT_ID_GOOGLE_AUTH = "CLIENT_ID_GOOGLE_AUTH"
    const val KTOR_ENTRY_URL = "KTOR_ENTRY_URL"
    const val KTOR_ENTRY_URL_ANDROID = "KTOR_ENTRY_URL_ANDROID"
    const val KTOR_ENTRY_URL_IOS = "KTOR_ENTRY_URL_IOS"
    const val IOS_CLIENT_ID = "IOS_CLIENT_ID"
    const val IOS_REVERSE_CLIENT_ID = "IOS_REVERSE_CLIENT_ID"
}

buildkonfig {
    packageName = "com.alwinsden.dino"
    val secretPropsFile = rootProject.file("secret.properties")

    class TypeDef {
        val string = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
    }

    val secretProps = Properties()
    if (secretPropsFile.exists()) {
        secretProps.load(secretPropsFile.inputStream())
    }
    defaultConfigs {
        buildConfigField(
            TypeDef().string,
            KeyStoreValues.CLIENT_ID_GOOGLE_AUTH,
            secretProps.getProperty(KeyStoreValues.CLIENT_ID_GOOGLE_AUTH)
        )

        buildConfigField(
            TypeDef().string,
            KeyStoreValues.KTOR_ENTRY_URL,
            secretProps.getProperty(KeyStoreValues.KTOR_ENTRY_URL_IOS)
        )

        //OS-based type differentiation
        targetConfigs {
            create("android") {
                buildConfigField(
                    TypeDef().string,
                    KeyStoreValues.KTOR_ENTRY_URL,
                    secretProps.getProperty(KeyStoreValues.KTOR_ENTRY_URL_ANDROID)
                )
            }
            create("ios") {
                buildConfigField(
                    TypeDef().string,
                    KeyStoreValues.KTOR_ENTRY_URL,
                    secretProps.getProperty(KeyStoreValues.KTOR_ENTRY_URL_IOS)
                )
            }
        }
    }
}

tasks.register("XCodeBuildKonfigGenerator") {
    val secretPropsFile = rootProject.file("secret.properties")
    val secretProps = Properties()
    if (secretPropsFile.exists()) {
        secretProps.load(secretPropsFile.inputStream())
    }
    val xcodeConfigFile = rootProject.file("iosApp/Configuration/Google.xcconfig")
    doLast {
        val writeContent = """
            ${KeyStoreValues.IOS_CLIENT_ID} = ${secretProps.getProperty(KeyStoreValues.IOS_CLIENT_ID)}
            ${KeyStoreValues.IOS_REVERSE_CLIENT_ID} = ${secretProps.getProperty(KeyStoreValues.IOS_REVERSE_CLIENT_ID)}
            ${KeyStoreValues.CLIENT_ID_GOOGLE_AUTH} = ${secretProps.getProperty(KeyStoreValues.CLIENT_ID_GOOGLE_AUTH)}
        """.trimIndent()
        if (!xcodeConfigFile.exists() || xcodeConfigFile.readText() != writeContent) {
            xcodeConfigFile.writeText(writeContent)
            println("Updated xcodeConfigFile!")
        } else {
            println("Config.xcconfig already at latest.")
        }
    }
}