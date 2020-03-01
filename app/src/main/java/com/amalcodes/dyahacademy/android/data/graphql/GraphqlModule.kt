package com.amalcodes.dyahacademy.android.data.graphql

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 01/03/20
 */

val graphqlModule = module {
    single { apolloClient(get(named("graphqlUrl")), get()) }
    single { DyahAcademyGraphql(get()) }
}

private fun apolloClient(
    serverUrl: String,
    httpClient: OkHttpClient
): ApolloClient = ApolloClient.builder()
    .serverUrl(serverUrl)
    .okHttpClient(httpClient)
    .logger { priority, message, t, args ->
        if (t.isPresent) {
            Timber.log(priority, t.get(), message, args)
        } else {
            Timber.log(priority, message, args)
        }
    }
    .build()