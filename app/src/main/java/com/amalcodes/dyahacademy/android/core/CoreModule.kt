package com.amalcodes.dyahacademy.android.core

import android.content.Context
import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.analytics.analyticsModule
import com.amalcodes.dyahacademy.android.core.initializer.*
import com.amalcodes.dyahacademy.android.data.api.apiModule
import com.amalcodes.dyahacademy.android.data.graphql.graphqlModule
import com.amalcodes.dyahacademy.android.data.repository.repositoryModule
import com.google.firebase.analytics.FirebaseAnalytics
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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
    single { markwon(androidContext()) }
} + listOf(
    graphqlModule,
    repositoryModule,
    apiModule,
    analyticsModule
)

private fun markwon(context: Context): Markwon = Markwon.builder(context)
    .usePlugins(
        listOf(
            CoilImagesPlugin.create(context),
            HtmlPlugin.create(),
            JLatexMathPlugin.create(context.resources.getDimension(R.dimen.font_size_base))
        )
    )
    .build()

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