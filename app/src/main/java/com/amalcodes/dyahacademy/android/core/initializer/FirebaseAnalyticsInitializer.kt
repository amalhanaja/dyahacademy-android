package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application
import com.amalcodes.dyahacademy.android.core.isProduction
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class FirebaseAnalyticsInitializer(
    private val firebaseAnalytics: FirebaseAnalytics
) : AppInitializer {

    override fun initialize(app: Application) = firebaseAnalytics.run {
        setAnalyticsCollectionEnabled(isProduction)
    }
}