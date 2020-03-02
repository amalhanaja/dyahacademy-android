package com.amalcodes.dyahacademy.android.features.topic.usecase

import com.amalcodes.dyahacademy.android.domain.model.Topic
import com.amalcodes.dyahacademy.android.domain.repository.TopicRepository
import com.amalcodes.dyahacademy.android.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


class GetTopicsUseCase(
    private val topicRepository: TopicRepository
) : UseCase<GetTopicsUseCase.Input, List<Topic>> {
    override fun invoke(input: Input): Flow<List<Topic>> {
        return topicRepository.listByCourseId(input.courseId)
    }

    data class Input(val courseId: String)
}