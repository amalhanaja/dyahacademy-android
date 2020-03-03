package com.amalcodes.dyahacademy.android.features.lesson

import androidx.lifecycle.viewModelScope
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.core.BaseViewModel
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.features.lesson.usecase.GetLessonsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class LessonListViewModel(
    private val getLessonsUseCase: GetLessonsUseCase,
    analytics: Analytics
) : BaseViewModel(analytics) {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is LessonListUIEvent.Fetch -> fetch(event.topicId, UIState.Loading)
            is LessonListUIEvent.RetryFailure -> fetch(event.topicId, UIState.Loading)
            is LessonListUIEvent.Refresh -> fetch(event.topicId, UIState.Refreshing)
            is LessonListUIEvent.GoToQuiz -> _uiState.postValue(
                LessonListUIState.GoToQuiz(_uiState.value, event.lessonViewEntity)
            )
            is LessonListUIEvent.GoToYoutube -> _uiState.postValue(
                LessonListUIState.GoToYoutube(_uiState.value, event.lessonViewEntity)
            )
            is UIEvent.RestoreUIState -> _uiState.postValue(event.uiState)
            else -> event.unhandled()
        }
    }

    @ExperimentalCoroutinesApi
    private fun fetch(topicId: String, loadingState: UIState) {
        getLessonsUseCase(GetLessonsUseCase.Input(topicId))
            .map { list -> list.map { it.toLessonViewEntity() } }
            .map { LessonListUIState.Content(it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(loadingState) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }
}
