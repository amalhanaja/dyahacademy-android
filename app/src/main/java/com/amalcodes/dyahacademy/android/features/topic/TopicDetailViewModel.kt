package com.amalcodes.dyahacademy.android.features.topic

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.GetTopicByIdQuery
import com.amalcodes.dyahacademy.android.features.lesson.LessonType
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewEntity
import com.amalcodes.dyahacademy.android.type.ENUM_LESSON_LESSONTYPE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class TopicDetailViewModel : ViewModel() {

    private val _uiState: MutableLiveData<TopicDetailUIState> = MutableLiveData(
        TopicDetailUIState.Initial
    )

    val uiState: LiveData<TopicDetailUIState> = _uiState

    @ExperimentalCoroutinesApi
    fun fetch(topicId: String) {
        TopicRepository.getTopicById(topicId)
            .map { topic ->
                val lessons = mutableListOf<LessonViewEntity>()
                topic.lessons().orEmpty().forEach { lesson ->
                    lessons.add(lesson.toLessonViewEntity())
                    lessons.addAll(
                        lesson.lessons().orEmpty().map { lesson1 ->
                            lesson1.toLessonViewEntity()
                        })
                }
                lessons
            }.map { list ->
                TopicDetailUIState.HasData(data = list)
            }
            .onStart { _uiState.postValue(TopicDetailUIState.Loading) }
            .catch { _uiState.postValue(TopicDetailUIState.Error(it)) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }

    private fun GetTopicByIdQuery.Lesson1.toLessonViewEntity(): LessonViewEntity {
        return when (lessonType()) {
            ENUM_LESSON_LESSONTYPE.YOUTUBE -> LessonViewEntity(
                id = id(),
                title = title(),
                type = LessonType.YOUTUBE,
                youtubeUrl = youtubeUrl()
            )
            ENUM_LESSON_LESSONTYPE.LESSON_GROUP -> LessonViewEntity(
                id = id(),
                title = title(),
                type = LessonType.LESSON_GROUP
            )
            ENUM_LESSON_LESSONTYPE.QUIZ -> LessonViewEntity(
                id = id(),
                title = title(),
                type = LessonType.QUIZ
            )
            else -> throw IllegalStateException("unsupported Lesson Type ${lessonType()}")
        }
    }

    private fun GetTopicByIdQuery.Lesson.toLessonViewEntity(): LessonViewEntity {
        return when (lessonType()) {
            ENUM_LESSON_LESSONTYPE.YOUTUBE -> LessonViewEntity(
                id = id(),
                title = title(),
                type = LessonType.YOUTUBE,
                youtubeUrl = youtubeUrl()
            )
            ENUM_LESSON_LESSONTYPE.LESSON_GROUP -> LessonViewEntity(
                id = id(),
                title = title(),
                type = LessonType.LESSON_GROUP
            )
            ENUM_LESSON_LESSONTYPE.QUIZ -> LessonViewEntity(
                id = id(),
                title = title(),
                type = LessonType.QUIZ
            )
            else -> throw IllegalStateException("unsupported Lesson Type ${lessonType()}")
        }
    }

    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TopicDetailViewModel() as T
        }
    }
}
