package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.dyahacademy.android.GetTopicByIdQuery
import com.amalcodes.dyahacademy.android.core.Injector
import com.amalcodes.dyahacademy.android.data.asFlow
import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


object TopicRepository {
    private val apolloClient: ApolloClient by lazy {
        Injector.apolloClient
    }

    @ExperimentalCoroutinesApi
    fun getTopicById(topicId: String): Flow<GetTopicByIdQuery.Topic> {
        val query = GetTopicByIdQuery.builder()
            .id(topicId)
            .build()
        return apolloClient.query(query)
            .asFlow()
            .map { requireNotNull(it.data()?.topic()) }
    }
}