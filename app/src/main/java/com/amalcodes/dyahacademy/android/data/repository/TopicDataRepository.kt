package com.amalcodes.dyahacademy.android.data.repository

import com.amalcodes.dyahacademy.android.data.graphql.DyahAcademyGraphql
import com.amalcodes.dyahacademy.android.data.repository.mapper.toTopic
import com.amalcodes.dyahacademy.android.domain.mapIfNotEmpty
import com.amalcodes.dyahacademy.android.domain.model.Topic
import com.amalcodes.dyahacademy.android.domain.repository.TopicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class TopicDataRepository(
    private val dyahAcademyGraphql: DyahAcademyGraphql
) : TopicRepository {

    override fun listByCourseId(courseId: String): Flow<List<Topic>> {
        return dyahAcademyGraphql.getTopics(courseId)
            .map { list -> list.mapIfNotEmpty { it.toTopic() } }
    }
}