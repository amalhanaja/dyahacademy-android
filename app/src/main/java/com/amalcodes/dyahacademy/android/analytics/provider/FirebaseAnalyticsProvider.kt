package com.amalcodes.dyahacademy.android.analytics.provider

import android.app.Activity
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class FirebaseAnalyticsProvider(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsProvider {

    override fun trackScreen(activity: Activity, screenName: String) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, screenName)
    }

    override fun trackEvent(name: String, properties: Map<String, Any?>) {
        firebaseAnalytics.logEvent(name, bundleOf(*properties.toList().toTypedArray()))
    }
}