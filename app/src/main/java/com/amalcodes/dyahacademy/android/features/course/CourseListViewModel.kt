package com.amalcodes.dyahacademy.android.features.course

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.core.trackEvent
import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CourseListViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    analytics: Analytics
) : ViewModel() {

    private val _uiState: MutableLiveData<UIState> = MutableLiveData(UIState.Initial)

    val uiState: LiveData<UIState> = _uiState.distinctUntilChanged()

    private val _event: MutableLiveData<UIEvent> = MutableLiveData()

    fun dispatch(event: UIEvent) = _event.postValue(event)

    init {
        _event.asFlow()
            .trackEvent(analytics)
            .onEach { handleEventChange(it) }
            .launchIn(viewModelScope)
    }

    private fun handleEventChange(event: UIEvent) {
        when (event) {
            is CourseListUIEvent.FetchCourses,
            is UIEvent.RetryFailure -> fetch()
            else -> throw IllegalStateException("Unhandled Event $event")
        }
    }

    @ExperimentalCoroutinesApi
    private fun fetch() {
        getCoursesUseCase(Unit)
            .map { list -> list.map { it.toCourseViewEntity() } }
            .map { CourseListUIState(it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}
