package com.amalcodes.dyahacademy.android.features.quiz.usecase

import com.amalcodes.dyahacademy.android.domain.model.Quiz
import com.amalcodes.dyahacademy.android.domain.repository.QuizRepository
import com.amalcodes.dyahacademy.android.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class GetQuizzesUseCase(
    private val quizRepository: QuizRepository
) : UseCase<GetQuizzesUseCase.Input, List<Quiz>> {

    override fun invoke(input: Input): Flow<List<Quiz>> =
        quizRepository.listByLessonId(input.lessonId)

    data class Input(val lessonId: String)
}