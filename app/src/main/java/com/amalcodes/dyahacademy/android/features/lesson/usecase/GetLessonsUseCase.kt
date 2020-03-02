package com.amalcodes.dyahacademy.android.features.lesson.usecase

import com.amalcodes.dyahacademy.android.domain.model.Lesson
import com.amalcodes.dyahacademy.android.domain.repository.LessonRepository
import com.amalcodes.dyahacademy.android.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class GetLessonsUseCase(
    private val lessonRepository: LessonRepository
) : UseCase<GetLessonsUseCase.Input, List<Lesson>> {

    override fun invoke(input: Input): Flow<List<Lesson>> {
        return lessonRepository.listByTopicId(input.topicId)
    }

    data class Input(val topicId: String)
}