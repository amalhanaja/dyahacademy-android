package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.viewModelScope
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.core.BaseViewModel
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CourseListViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    analytics: Analytics
) : BaseViewModel(analytics) {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is CourseListUIEvent.FetchCourses, is CourseListUIEvent.RetryFailure -> fetch()
            is CourseListUIEvent.GoToTopics -> _uiState.postValue(
                CourseListUIState.GoToTopics(_uiState.value, event.courseViewEntity)
            )
            is UIEvent.RestoreUIState -> _uiState.postValue(event.uiState)
            else -> event.unhandled()
        }
    }

    @ExperimentalCoroutinesApi
    private fun fetch() {
        getCoursesUseCase(Unit)
            .map { list -> list.map { it.toCourseViewEntity() } }
            .map { CourseListUIState.Content(it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}
