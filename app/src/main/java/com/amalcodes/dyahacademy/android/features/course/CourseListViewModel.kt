package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CourseListViewModel : ViewModel() {

    private val _uiState: MutableLiveData<CourseListUIState> = MutableLiveData(
        CourseListUIState.Initial
    )

    val uiState: LiveData<CourseListUIState> = _uiState

    @ExperimentalCoroutinesApi
    fun fetch() {
        viewModelScope.launch {
            CourseRepository.findAllCourse()
                .map { list ->
                    list.map {
                        CourseViewEntity(it.id(), it.title(), it.createdBy())
                    }
                }
                .map { CourseListUIState.HasData(it) }
                .catch { _uiState.postValue(CourseListUIState.Error(it)) }
                .collect {
                    _uiState.postValue(it)
                }
        }
    }

    object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CourseListViewModel() as T

        }
    }
}
