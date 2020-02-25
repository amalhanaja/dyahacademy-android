package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CourseListViewModel : ViewModel() {

    private val _uiState: MutableLiveData<CourseListUIState> = MutableLiveData(
        CourseListUIState.Initial
    )

    val uiState: LiveData<CourseListUIState> = _uiState

    @ExperimentalCoroutinesApi
    fun fetch() {
        CourseRepository.findAllCourse()
            .map { list ->
                list.map {
                    CourseViewEntity(it.id(), it.title(), it.createdBy())
                }
            }
            .map { CourseListUIState.HasData(it) }
            .onStart { _uiState.postValue(CourseListUIState.Loading) }
            .catch { _uiState.postValue(CourseListUIState.Error(it)) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }

    object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CourseListViewModel() as T

        }
    }
}
