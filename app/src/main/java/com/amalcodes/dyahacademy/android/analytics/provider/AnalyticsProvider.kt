package com.amalcodes.dyahacademy.android.analytics.provider

import android.app.Activity

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


interface AnalyticsProvider {
    fun trackScreen(activity: Activity, screenName: String)
    fun trackEvent(name: String, properties: Map<String, Any?> = emptyMap())
}