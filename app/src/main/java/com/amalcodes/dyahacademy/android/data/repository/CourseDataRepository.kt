package com.amalcodes.dyahacademy.android.data.repository

import com.amalcodes.dyahacademy.android.data.graphql.DyahAcademyGraphql
import com.amalcodes.dyahacademy.android.data.repository.mapper.toCourse
import com.amalcodes.dyahacademy.android.domain.mapIfNotEmpty
import com.amalcodes.dyahacademy.android.domain.model.Course
import com.amalcodes.dyahacademy.android.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class CourseDataRepository(
    private val dyahAcademyGraphql: DyahAcademyGraphql
) : CourseRepository {

    override fun list(): Flow<List<Course>> {
        return dyahAcademyGraphql.getCourses()
            .map { list -> list.mapIfNotEmpty { it.toCourse() } }
    }
}