import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")

    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

    id("org.jetbrains.kotlin.plugin.compose")
}

val localProps = Properties().apply {
    load(File("local.properties").inputStream())
}
fun getApiKey(key: String) = localProps.getProperty(key, "") ?: ""

android {
    namespace = "com.store_me.storeme"
    compileSdk = 35
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.store_me.storeme"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "NAVER_CLIENT_SECRET", getApiKey("naver_client_secret"))
        buildConfigField("String", "NAVER_CLIENT_ID", getApiKey("naver_client_id"))
        buildConfigField("String", "KAKAO_KEY", "\"${getApiKey("kakao_key")}\"")
    }

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    signingConfigs {
        create("release") {
            keyAlias = properties.getProperty("key_alias")
            keyPassword = properties.getProperty("store_password")
            storeFile = file(properties.getProperty("store_file"))
            storePassword = properties.getProperty("store_password")
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders["KAKAO_KEY"] = getApiKey("kakao_key")
            manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = getApiKey("naver_map_client_id")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["KAKAO_KEY"] = getApiKey("kakao_key")
            manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = getApiKey("naver_map_client_id")
            manifestPlaceholders["NAVER_MAP_CLIENT_SECRET"] = getApiKey("naver_map_client_secret")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.21"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2025.10.01")
    implementation(composeBom)
    testImplementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.appcompat:appcompat:1.7.0")

    //Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    //Permission
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    //liveData
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")

    //Test
    testImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.compose.ui:ui-test")

    //EncryptedSharedPreferences
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha07")

    //Kakao Login
    implementation("com.kakao.sdk:v2-user:2.22.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    //Location
    implementation ("com.google.android.gms:play-services-location:21.3.0")


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.6.4")

    //Map
    implementation("io.github.fornewid:naver-map-compose:1.7.3")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-appcheck-debug")


    //Local Data Storage
    implementation("androidx.datastore:datastore-preferences:1.1.4")

    //Load Url Image
    implementation("io.coil-kt:coil-compose:2.7.0")

    //rich-editor
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc13")

    //reorderable
    implementation("sh.calvin.reorderable:reorderable:3.0.0")

    //ExoPlayer
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    //UCrop
    implementation("com.github.yalantis:ucrop:2.2.10")

    //Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.7.0")
}