plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath("com.google.gms:google-services:4.4.2")
    }
}