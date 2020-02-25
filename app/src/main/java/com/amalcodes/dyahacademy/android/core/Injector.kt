package com.amalcodes.dyahacademy.android.core

import com.amalcodes.dyahacademy.android.data.FlowCallAdapterFactory
import com.apollographql.apollo.ApolloClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.noties.markwon.Markwon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */

object Injector {

    lateinit var markwon: Markwon

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHTTP").d(message)
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("http://192.168.0.6:1339/")
        .addCallAdapterFactory(FlowCallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl("http://192.168.0.6:1339/graphql")
        .logger { priority, message, t, args ->
            if (t.isPresent) {
                Timber.log(priority, t.get(), message, args)
            } else {
                Timber.log(priority, message, args)
            }
        }
        .build()
}