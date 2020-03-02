package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.analytics.AnalyticsLifecycleCallback

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class LifecycleCallbackInitializer(
    private val analytics: Analytics
) : AppInitializer {
    override fun initialize(app: Application) {
        app.registerActivityLifecycleCallbacks(AnalyticsLifecycleCallback(analytics))
    }
}