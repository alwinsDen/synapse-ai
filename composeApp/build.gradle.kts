import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.buildKonfig)
    kotlin("plugin.serialization").version("2.3.0")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            val credentialVersion = "1.6.0-rc01"
            implementation("androidx.credentials:credentials:$credentialVersion")
            implementation("androidx.credentials:credentials-play-services-auth:$credentialVersion")
            implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
            implementation(libs.navigation.compose)
            implementation(libs.material.icons.core)
            implementation(libs.material.icons.extended)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.alwinsden.dino"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.alwinsden.dino"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
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

//this is the process that trigger build + inject of Kotlin UI at IOS level.
tasks.named("embedAndSignAppleFrameworkForXcode") {
    dependsOn("XCodeBuildKonfigGenerator")
}