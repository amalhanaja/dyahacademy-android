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

    private val jsonCustomTypeAdapter = object : CustomTypeAdapter<Any> {
        override fun decode(value: CustomTypeValue<*>): Any {
            return when (value) {
                is CustomTypeValue.GraphQLJsonList -> value.value
                is CustomTypeValue.GraphQLJsonObject -> value.value
                else -> throw IllegalStateException()
            }
        }

        override fun encode(value: Any): CustomTypeValue<*> {
            return when (value) {
                is List<*> -> CustomTypeValue.GraphQLJsonList(value)
                is Map<*, *> -> CustomTypeValue.GraphQLJsonObject(value as Map<String, Any>)
                else -> throw IllegalStateException("")
            }
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