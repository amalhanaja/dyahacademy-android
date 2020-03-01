package com.amalcodes.dyahacademy.android.core

import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.core.initializer.AppInitializers
import com.amalcodes.dyahacademy.android.core.initializer.FirebaseAnalyticsInitializer
import com.amalcodes.dyahacademy.android.core.initializer.FirebaseCrashlyticsInitializer
import com.amalcodes.dyahacademy.android.core.initializer.TimberInitializer
import com.amalcodes.dyahacademy.android.data.api.apiModule
import com.amalcodes.dyahacademy.android.data.graphql.graphqlModule
import com.amalcodes.dyahacademy.android.data.repository.repositoryModule
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
    single { appInitializers() }
} + listOf(graphqlModule, repositoryModule, apiModule)

private fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHTTP").d(message)
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY)
    )
    .build()

private fun appInitializers(): AppInitializers = AppInitializers(
    initializers = setOf(
        TimberInitializer(),
        FirebaseCrashlyticsInitializer(),
        FirebaseAnalyticsInitializer()
    )
)