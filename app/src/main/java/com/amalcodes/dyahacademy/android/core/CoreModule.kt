package com.amalcodes.dyahacademy.android.core

import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.analytics.analyticsModule
import com.amalcodes.dyahacademy.android.core.initializer.*
import com.amalcodes.dyahacademy.android.data.api.apiModule
import com.amalcodes.dyahacademy.android.data.graphql.graphqlModule
import com.amalcodes.dyahacademy.android.data.repository.repositoryModule
import com.google.firebase.analytics.FirebaseAnalytics
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


val coreModules = module {
    single { okHttpClient() }
    single(named("baseUrl")) { BuildConfig.BASE_URL }
    single(named("graphqlUrl")) { get<String>(named("baseUrl")) + "graphql" }
    single { appInitializers(get(), get()) }
} + listOf(
    graphqlModule,
    repositoryModule,
    apiModule,
    analyticsModule
)

private fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHTTP").d(message)
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY)
    )
    .build()

private fun appInitializers(
    firebaseAnalytics: FirebaseAnalytics,
    analytics: Analytics
): AppInitializers = AppInitializers(
    initializers = setOf(
        TimberInitializer(),
        FirebaseCrashlyticsInitializer(),
        FirebaseAnalyticsInitializer(firebaseAnalytics),
        LifecycleCallbackInitializer(analytics)
    )
)