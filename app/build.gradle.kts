import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    kotlin("android") version "1.3.61"
    kotlin("android.extensions") version "1.3.61"
    kotlin("kapt") version "1.3.61"
    id("androidx.navigation.safeargs.kotlin")
    id("com.apollographql.apollo") version "1.3.2"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdkVersion(29)
    flavorDimensions("default")
    buildToolsVersion = "29.0.2"

    defaultConfig {
        applicationId = "com.amalcodes.dyahacademy.android"
        minSdkVersion(21)
        targetSdkVersion(29)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(stringProperty("${name}.storeFile"))
            storePassword = stringProperty("${name}.storePassword")
            keyAlias = stringProperty("${name}.keyAlias")
            keyPassword = stringProperty("${name}.keyPassword")
        }
        create("release") {
            if (!env("RELEASE_STORE_FILE").isNullOrEmpty()) {
                storeFile = file(mustEnv("RELEASE_STORE_FILE"))
                storePassword = mustEnv("RELEASE_STORE_PASSWORD")
                keyAlias = mustEnv("RELEASE_KEY_ALIAS")
                keyPassword = mustEnv("RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isCrunchPngs = true
            isZipAlignEnabled = true
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
        }
        getByName("debug") {
            isCrunchPngs = true
            isZipAlignEnabled = false
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    productFlavors {
        create("development") {
            versionCode = 1
            versionName = "1.0"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-DEV"
            resValue("string", "app_name", stringProperty("${name}.appName"))
            buildConfigField("String", "BASE_URL", stringProperty("${name}.baseUrl"))
        }
        create("production") {
            versionCode = 1
            versionName = "1.0"
            resValue("string", "app_name", stringProperty("${name}.appName"))
            buildConfigField("String", "BASE_URL", stringProperty("${name}.baseUrl"))
        }
    }

    applicationVariants.all variant@{
        outputs.all {
            val outputImpl = this as BaseVariantOutputImpl
            val versionName = this@variant.versionName
            val versionCode = this@variant.versionCode
            val flavorName = this@variant.flavorName
            val buildType = this@variant.buildType.name
            val appName = stringProperty("${flavorName}.appName")
            val fileName = "$appName-$versionName-$versionCode-$buildType"
            outputImpl.outputFileName = fileName + ".${outputFile.extension}"
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
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.7.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.4.0")
    implementation("com.squareup.moshi:moshi:1.9.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    implementation("com.google.firebase:firebase-analytics:17.2.2")
    implementation("com.google.firebase:firebase-crashlytics:17.0.0-beta01")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.2")
    implementation("io.noties.markwon:core:4.2.1")
    implementation("io.noties.markwon:image-coil:4.2.1")
    implementation("io.coil-kt:coil:0.9.5")
    debugImplementation("com.amitshekhar.android:debug-db:1.0.6")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    compileOnly("org.jetbrains:annotations:16.0.1")
}
