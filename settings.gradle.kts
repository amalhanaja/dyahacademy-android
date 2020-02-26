pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            val pluginId = requested.id.id
            if (pluginId == "com.android.application") {
                useModule("com.android.tools.build:gradle:3.6.0")
            }
            if (pluginId == "androidx.navigation.safeargs.kotlin") {
                useModule("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.0")
            }
            if (pluginId == "com.google.gms.google-services") {
                useModule("com.google.gms:google-services:4.3.3")
            }
            if (pluginId == "com.google.firebase.crashlytics") {
                useModule("com.google.firebase:firebase-crashlytics-gradle:2.0.0-beta02")
            }
        }
    }
}

include(":app")
rootProject.name = "DyahAcademy"
