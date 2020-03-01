package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CourseListViewModel(
    private val getCoursesUseCase: GetCoursesUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<UIState> = MutableLiveData(UIState.Initial)

    val uiState: LiveData<UIState> = _uiState

    @ExperimentalCoroutinesApi
    fun fetch() {
        getCoursesUseCase(Unit)
            .map { list -> list.map { it.toCourseViewEntity() } }
            .map { CourseListUIState(it) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .catch { _uiState.postValue(it.toUIState()) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}
