package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.GetTopicByIdQuery
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewEntity
import com.amalcodes.dyahacademy.android.features.topic.TopicRepository
import com.amalcodes.dyahacademy.android.features.topic.TopicViewEntity
import com.amalcodes.dyahacademy.android.type.ENUM_LESSON_LESSONTYPE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CourseDetailViewModel : ViewModel() {

    private val _uiState: MutableLiveData<CourseDetailUIState> = MutableLiveData(
        CourseDetailUIState.Initial
    )

    val uiState: LiveData<CourseDetailUIState> = _uiState

    @ExperimentalCoroutinesApi
    fun fetch(courseId: String) {
        viewModelScope.launch {
            CourseRepository.getDetail(courseId)
                .map { course ->
                    CourseDetailViewEntity(
                        title = requireNotNull(course.title()),
                        description = "",
                        topics = course.topics().orEmpty().map { topic ->
                            TopicViewEntity(
                                id = requireNotNull(topic.id()),
                                title = requireNotNull(topic.title()),
                                description = topic.description().orEmpty()
                            )
                        }
                    )
                }
                .flatMapLatest {
                    var data = it.copy(topics = emptyList())
                    it.topics.forEach { topic ->
                        val lessons = TopicRepository.getTopicById(topic.id)
                            .singleOrNull()?.lessons().orEmpty().map { lesson ->
                                lesson.toLessonViewEntity()
                            }
                        data =
                            data.copy(topics = data.topics + listOf(topic.copy(lessons = lessons)))
                    }
                    flowOf(data)
                }
                .map { CourseDetailUIState.HasData(it) }
                .catch { _uiState.postValue(CourseDetailUIState.Error(it)) }
                .collect {
                    _uiState.postValue(it)
                }
        }
    }

    private fun GetTopicByIdQuery.Lesson.toLessonViewEntity(): LessonViewEntity {
        return when (lessonType()) {
            ENUM_LESSON_LESSONTYPE.YOUTUBE -> LessonViewEntity.Youtube(
                id = id(),
                title = title(),
                youtubeUrl = requireNotNull(youtubeUrl())
            )
            ENUM_LESSON_LESSONTYPE.DOCUMENT -> LessonViewEntity.Document(
                id = id(),
                title = title(),
                url = requireNotNull(document()?.url())
            )
            ENUM_LESSON_LESSONTYPE.MARKDOWN -> LessonViewEntity.Markdown(
                id = id(),
                title = title(),
                content = markdown().orEmpty()
            )
            ENUM_LESSON_LESSONTYPE.QUIZ -> LessonViewEntity.Quiz(
                id = id(),
                title = title()
            )
            else -> throw IllegalStateException("unsupported Lesson Type ${lessonType()}")
        }
    }

    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CourseDetailViewModel() as T

        }
    }
}
