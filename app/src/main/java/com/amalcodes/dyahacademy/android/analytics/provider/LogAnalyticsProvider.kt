package com.amalcodes.dyahacademy.android.analytics.provider

import android.app.Activity
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class LogAnalyticsProvider(private val priority: Int) : AnalyticsProvider {

    override fun trackScreen(activity: Activity, screenName: String) {
        Timber.tag("TrackScreen").log(priority, screenName)
    }

    override fun trackEvent(name: String, properties: Map<String, Any?>) {
        Timber.tag("TrackEvent").log(priority, "$name: $properties")
    }
}