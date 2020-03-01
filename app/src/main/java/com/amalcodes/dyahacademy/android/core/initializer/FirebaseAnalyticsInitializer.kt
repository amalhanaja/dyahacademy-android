package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application
import com.amalcodes.dyahacademy.android.core.isProduction
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class FirebaseAnalyticsInitializer : AppInitializer {
    override fun initialize(app: Application) = FirebaseAnalytics.getInstance(app).run {
        setAnalyticsCollectionEnabled(isProduction)
    }
}