package com.amalcodes.dyahacademy.android.domain.repository

import com.amalcodes.dyahacademy.android.domain.model.Topic
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


interface TopicRepository {
    fun listByCourseId(courseId: String): Flow<List<Topic>>
}