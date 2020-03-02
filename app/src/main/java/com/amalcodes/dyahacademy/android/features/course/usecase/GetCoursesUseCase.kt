package com.amalcodes.dyahacademy.android.features.course.usecase

import com.amalcodes.dyahacademy.android.domain.model.Course
import com.amalcodes.dyahacademy.android.domain.repository.CourseRepository
import com.amalcodes.dyahacademy.android.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class GetCoursesUseCase(
    private val courseRepository: CourseRepository
) : UseCase<Unit, List<Course>> {
    override fun invoke(input: Unit): Flow<List<Course>> {
        return courseRepository.list()
    }
}