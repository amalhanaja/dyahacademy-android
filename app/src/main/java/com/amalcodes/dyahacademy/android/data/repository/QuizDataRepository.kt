package com.amalcodes.dyahacademy.android.data.repository

import com.amalcodes.dyahacademy.android.data.api.DyahAcademyApi
import com.amalcodes.dyahacademy.android.data.repository.mapper.toQuiz
import com.amalcodes.dyahacademy.android.domain.mapIfNotEmpty
import com.amalcodes.dyahacademy.android.domain.model.LessonType
import com.amalcodes.dyahacademy.android.domain.model.Quiz
import com.amalcodes.dyahacademy.android.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class QuizDataRepository(
    private val dyahAcademyApi: DyahAcademyApi
) : QuizRepository {
    override fun listByLessonId(lessonId: String): Flow<List<Quiz>> {
        return dyahAcademyApi.getLesson(lessonId)
            .filter { it.lessonType in listOf(LessonType.QUIZ.name, LessonType.TEST.name) }
            .map { lesson -> lesson.quizzes.orEmpty().mapIfNotEmpty { it.toQuiz() } }
    }
}