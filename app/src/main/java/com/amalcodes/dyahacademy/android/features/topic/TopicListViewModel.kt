package com.amalcodes.dyahacademy.android.features.topic

import androidx.lifecycle.viewModelScope
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.core.BaseViewModel
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.features.topic.usecase.GetTopicsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


@ExperimentalCoroutinesApi
class TopicListViewModel(
    analytics: Analytics,
    private val getTopicsUseCase: GetTopicsUseCase
) : BaseViewModel(analytics) {

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is TopicListUIEvent.Fetch -> fetch(event.courseId, UIState.Loading)
            is TopicListUIEvent.RetryFailure -> fetch(event.courseId, UIState.Loading)
            is TopicListUIEvent.Refresh -> fetch(event.courseId, UIState.Refreshing)
            is UIEvent.RestoreUIState -> _uiState.postValue(event.uiState)
            is TopicListUIEvent.GoToLessons -> _uiState.postValue(
                TopicListUIState.GoToLessons(_uiState.value, event.topicViewEntity)
            )
            else -> event.unhandled()
        }
    }

    @ExperimentalCoroutinesApi
    private fun fetch(courseId: String, loadingState: UIState) {
        getTopicsUseCase(GetTopicsUseCase.Input(courseId))
            .map { list -> list.map { it.toTopicViewEntity() } }
            .map { TopicListUIState.Content(it) as UIState }
            .catch { emit(it.toUIState()) }
            .onStart { _uiState.postValue(loadingState) }
            .onEach { _uiState.postValue(it) }
            .launchIn(viewModelScope)
    }

}
