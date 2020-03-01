package com.amalcodes.dyahacademy.android.domain.repository

import com.amalcodes.dyahacademy.android.domain.model.Quiz
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


interface QuizRepository {
    fun listByLessonId(lessonId: String): Flow<List<Quiz>>
}