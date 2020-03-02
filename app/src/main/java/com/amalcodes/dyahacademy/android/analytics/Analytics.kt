package com.amalcodes.dyahacademy.android.analytics

import android.app.Activity
import com.amalcodes.dyahacademy.android.analytics.provider.AnalyticsProvider

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class Analytics(
    private val providers: Set<AnalyticsProvider>
) {

    fun trackScreen(
        activity: Activity,
        screenName: String
    ) = providers.forEach { it.trackScreen(activity, screenName) }

    fun trackEvent(event: Event) = providers.forEach {
        it.trackEvent(event.name, event.properties)
    }

}