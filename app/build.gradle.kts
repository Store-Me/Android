import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")

    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")

    id("org.jetbrains.kotlin.plugin.compose")
}

fun getApiKey(propertyKey: String): String {
    val properties = Properties()
    val localPropertiesFile = File(rootDir, "local.properties")

    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }

    return properties.getProperty(propertyKey, "") ?: ""
}

android {
    namespace = "com.store_me.storeme"
    compileSdk = 34
    buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.store_me.storeme"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "NAVER_CLIENT_SECRET", getApiKey("naver_client_secret"))
        buildConfigField("String", "NAVER_CLIENT_ID", getApiKey("naver_client_id"))
        val kakaoKey = getApiKey("kakao_key")
        buildConfigField("String", "KAKAO_KEY", "\"${kakaoKey}\"")
    }

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    android {
        signingConfigs {
            create("release") {
                keyAlias = properties.getProperty("key_alias")
                keyPassword = properties.getProperty("store_password")
                storeFile = file(properties.getProperty("store_file"))
                storePassword = properties.getProperty("store_password")
            }
        }
    }


    buildTypes {
        debug {
            manifestPlaceholders["KAKAO_KEY"] = getApiKey("kakao_key")
            manifestPlaceholders["NAVER_MAP_CLIENT_ID"] = properties.getProperty("naver_map_client_id", "")
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
    implementation("androidx.compose.animation:animation:1.7.0-rc01")
    implementation("androidx.compose.foundation:foundation:1.7.0-rc01")
    implementation("androidx.compose.ui:ui:1.7.0-rc01")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-rc01")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.material3:material3:1.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //EncryptedSharedPreferences
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    //Kakao Login
    implementation("com.kakao.sdk:v2-user:2.14.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    kapt("com.google.dagger:hilt-compiler:2.48.1")
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
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-appcheck-debug")


    //Local Data Storage
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Load Url Image
    implementation("io.coil-kt:coil-compose:2.7.0")

    //indicator
    implementation("com.google.accompanist:accompanist-pager:0.20.3")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.20.3")

    //liveData
    implementation("androidx.compose.runtime:runtime:1.6.8")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")

    //rich-editor
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc10")

    //reorderable
    implementation("sh.calvin.reorderable:reorderable:2.3.1")

    //ExoPlayer
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    //UCrop
    implementation("com.github.yalantis:ucrop:2.2.10")

    //Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.0.1")
}