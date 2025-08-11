plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

repositories {
    google()
    mavenCentral()
    // Remove jcenter() as it's deprecated and shut down since 2021; dependencies should resolve from google() or mavenCentral().
}

android {
    namespace = "com.example.glassgameboyemulator"
    compileSdk = 19

    defaultConfig {
        applicationId = "com.example.glassgameboyemulator"
        minSdk = 19
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 19
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.test.InstrumentationTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        debug {}
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_6 // Compatible with API 19
        targetCompatibility = JavaVersion.VERSION_1_6
    }

    kotlinOptions {
        jvmTarget = "1.6"
        freeCompilerArgs += "-Xno-inline"
        freeCompilerArgs += "-Xno-optimize"
        freeCompilerArgs += "-Xuse-ir"
        freeCompilerArgs += "-Xno-call-assertions"
        freeCompilerArgs += "-Xno-param-assertions"
        freeCompilerArgs += "-Xno-reified-type-parameters" // Disable reified types
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/jni/CMakeLists.txt")
        }
    }
}

dependencies {
    // Remove material:1.13.0 as recent versions require minSdk 21+ (incompatible with your API 19 target).
    // If you need Material Design, downgrade to an older support library like 'com.android.support:design:28.0.0',
    // but confirm usage—your MainActivity.kt doesn't appear to use it.
    implementation("com.android.support:support-v4:19.1.0")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:2.2.2")
}