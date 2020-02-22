plugins {
    id("com.android.application")
    kotlin("android") version "1.3.61"
    kotlin("android.extensions") version "1.3.61"
    id("androidx.navigation.safeargs.kotlin")
    id("com.apollographql.apollo") version "1.3.2"
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.2"
    defaultConfig {
        applicationId = "com.amalcodes.dyahacademy.android"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

apollo {
    customTypeMapping.put(
        "JSON", "org.json.JSONObject"
    )
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0")
    implementation("com.amalcodes.ezrecyclerview:ezrecyclerview-adapter:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.1")
    implementation("com.google.android.material:material:1.2.0-alpha05")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.apollographql.apollo:apollo-runtime:1.3.2")
    implementation("com.apollographql.apollo:apollo-coroutines-support:1.3.2")
    implementation("com.google.android.exoplayer:exoplayer:2.11.2")
    implementation("com.github.HaarigerHarald:android-youtubeExtractor:2.0.0")
    implementation("io.noties.markwon:core:4.2.1")
    implementation("io.noties.markwon:image-coil:4.2.1")
    implementation("io.coil-kt:coil:0.9.5")
    debugImplementation("com.amitshekhar.android:debug-db:1.0.6")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    compileOnly("org.jetbrains:annotations:16.0.1")
}
