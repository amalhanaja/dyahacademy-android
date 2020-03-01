package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.GetAllCoursesQuery
import com.amalcodes.dyahacademy.android.GetCourseByIdQuery
import com.amalcodes.dyahacademy.android.core.Injector
import com.amalcodes.dyahacademy.android.data.asFlow
import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


object CourseRepository {

    private val apolloClient: ApolloClient by lazy {
        Injector.apolloClient
    }

    @ExperimentalCoroutinesApi
    fun findAllCourse(): Flow<List<GetAllCoursesQuery.Course>> {
        val query: GetAllCoursesQuery = GetAllCoursesQuery.builder()
            .build()
        return apolloClient.query(query)
            .asFlow()
            .map {
                requireNotNull(it.data()?.courses())
            }
    }

    @ExperimentalCoroutinesApi
    fun getDetail(courseId: String): Flow<GetCourseByIdQuery.Course> {
        val query: GetCourseByIdQuery = GetCourseByIdQuery.builder()
            .id(courseId)
            .build()
        return apolloClient.query(query)
            .asFlow()
            .map { requireNotNull(it.data()?.course()) }
    }
}