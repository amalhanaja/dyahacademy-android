package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application
import com.amalcodes.dyahacademy.android.core.isProduction
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class FirebaseCrashlyticsInitializer : AppInitializer {
    override fun initialize(app: Application) {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isProduction)
    }
}