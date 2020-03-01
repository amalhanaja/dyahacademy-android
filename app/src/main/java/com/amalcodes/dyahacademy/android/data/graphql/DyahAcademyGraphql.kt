package com.amalcodes.dyahacademy.android.data.graphql

import com.amalcodes.dyahacademy.android.GetAllCoursesQuery
import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class DyahAcademyGraphql(private val apolloClient: ApolloClient) {

    fun getCourses(): Flow<List<GetAllCoursesQuery.Course>> {
        val query: GetAllCoursesQuery = GetAllCoursesQuery.builder().build()
        return apolloClient.query(query)
            .asFlow()
            .map { it.data()?.courses().orEmpty() }
    }
}