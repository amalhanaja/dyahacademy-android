package com.amalcodes.dyahacademy.android.core

import com.apollographql.apollo.ApolloClient
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */

object Injector {


    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl("http://192.168.0.6:1337/graphql")
        .logger { priority, message, t, args ->
            if (t.isPresent) {
                Timber.log(priority, t.get(), message, args)
            } else {
                Timber.log(priority, message, args)
            }
        }
        .build()
}