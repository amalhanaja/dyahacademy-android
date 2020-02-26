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
        }
    }
}

include(":app")
rootProject.name = "DyahAcademy"
