package com.amalcodes.dyahacademy.android.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


val apiModule = module {
    single { retrofit(get(named("baseUrl")), get(), get()) }
    single { moshi() }
    single<DyahAcademyApi> { get<Retrofit>().create() }
}

private fun moshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun retrofit(baseUrl: String, httpClient: OkHttpClient, moshi: Moshi) = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(baseUrl)
    .addCallAdapterFactory(FlowCallAdapterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()