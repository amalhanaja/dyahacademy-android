package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.features.topic.TopicViewEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CourseDetailViewModel : ViewModel() {

    private val _uiState: MutableLiveData<CourseDetailUIState> = MutableLiveData(
        CourseDetailUIState.Initial
    )

    val uiState: LiveData<CourseDetailUIState> = _uiState

    @ExperimentalCoroutinesApi
    fun fetch(courseId: String) {
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
            .map { CourseDetailUIState.HasData(it) }
            .onStart { _uiState.postValue(CourseDetailUIState.Loading) }
            .catch { _uiState.postValue(CourseDetailUIState.Error(it)) }
            .onEach {
                _uiState.postValue(it)
            }.launchIn(viewModelScope)
    }

    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CourseDetailViewModel() as T

        }
    }
}
