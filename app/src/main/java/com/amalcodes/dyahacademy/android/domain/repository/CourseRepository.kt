package com.amalcodes.dyahacademy.android.domain.repository

import com.amalcodes.dyahacademy.android.domain.model.Course
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


interface CourseRepository {
    fun list(): Flow<List<Course>>
}