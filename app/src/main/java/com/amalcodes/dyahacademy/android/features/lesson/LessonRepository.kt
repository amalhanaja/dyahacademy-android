package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.GetLessonByIdQuery
import com.amalcodes.dyahacademy.android.core.Injector
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */

object LessonRepository {

    private val apolloClient: ApolloClient by lazy {
        Injector.apolloClient
    }

    @ExperimentalCoroutinesApi
    fun getLessonById(lessonId: String): Flow<GetLessonByIdQuery.Lesson> {
        val query = GetLessonByIdQuery.builder()
            .id(lessonId)
            .build()
        return apolloClient.query(query)
            .toFlow()
            .map { requireNotNull(it.data()?.lesson()) }
    }
}