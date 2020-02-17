package com.amalcodes.dyahacademy.android.core

import com.amalcodes.dyahacademy.android.type.CustomType
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import io.noties.markwon.Markwon
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */

object Injector {

    lateinit var markwon: Markwon

    private val jsonCustomTypeAdapter = object : CustomTypeAdapter<Map<String, Any>> {
        override fun decode(value: CustomTypeValue<*>): Map<String, Any> {
            require(value is CustomTypeValue.GraphQLJsonObject)
            return value.value
        }

        override fun encode(value: Map<String, Any>): CustomTypeValue<*> {
            return CustomTypeValue.GraphQLJsonObject(value)
        }
    }

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl("http://192.168.0.6:1337/graphql")
        .logger { priority, message, t, args ->
            if (t.isPresent) {
                Timber.log(priority, t.get(), message, args)
            } else {
                Timber.log(priority, message, args)
            }
        }
        .addCustomTypeAdapter(CustomType.JSON, jsonCustomTypeAdapter)
        .build()
}