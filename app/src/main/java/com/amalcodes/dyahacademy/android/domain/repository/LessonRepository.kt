package com.amalcodes.dyahacademy.android.domain.repository

import com.amalcodes.dyahacademy.android.domain.model.Lesson
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


interface LessonRepository {
    fun listByTopicId(topicId: String): Flow<List<Lesson>>
}