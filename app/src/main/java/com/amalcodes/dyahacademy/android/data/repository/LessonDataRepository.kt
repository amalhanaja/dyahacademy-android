package com.amalcodes.dyahacademy.android.data.repository

import com.amalcodes.dyahacademy.android.data.graphql.DyahAcademyGraphql
import com.amalcodes.dyahacademy.android.data.repository.mapper.toLessons
import com.amalcodes.dyahacademy.android.domain.mapIfNotEmpty
import com.amalcodes.dyahacademy.android.domain.model.Lesson
import com.amalcodes.dyahacademy.android.domain.repository.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class LessonDataRepository(
    private val dyahAcademyGraphql: DyahAcademyGraphql
) : LessonRepository {

    override fun listByTopicId(topicId: String): Flow<List<Lesson>> {
        return dyahAcademyGraphql.getLessons(topicId)
            .map { lessons ->
                lessons.mapIfNotEmpty { it.toLessons() }
                    .reduce { acc, list -> acc + list }
            }
    }
}