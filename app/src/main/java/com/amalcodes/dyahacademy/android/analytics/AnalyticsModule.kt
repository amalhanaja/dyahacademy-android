package com.amalcodes.dyahacademy.android.analytics

import android.util.Log
import com.amalcodes.dyahacademy.android.analytics.provider.FirebaseAnalyticsProvider
import com.amalcodes.dyahacademy.android.analytics.provider.LogAnalyticsProvider
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


val analyticsModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
    single { analytics(get()) }
}

private fun analytics(firebaseAnalytics: FirebaseAnalytics): Analytics = Analytics(
    providers = setOf(
        FirebaseAnalyticsProvider(firebaseAnalytics),
        LogAnalyticsProvider(Log.INFO)
    )
)