package com.amalcodes.dyahacademy.android.core

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.analytics.Analytics
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


@ExperimentalCoroutinesApi
abstract class BaseViewModel(
    analytics: Analytics
) : ViewModel() {

    @Suppress("PropertyName")
    protected val _uiState: MediatorLiveData<UIState> = MediatorLiveData()

    private val _uiEvent: MutableLiveData<UIEvent> = MutableLiveData()

    init {
        _uiState.value = UIState.Initial
        _uiEvent.asFlow()
            .trackEvent(analytics)
            .onEach { handleEventChanged(it) }
            .launchIn(viewModelScope)
    }

    val uiState: LiveData<UIState> = _uiState

    protected abstract fun handleEventChanged(event: UIEvent)

    fun dispatch(event: UIEvent) = _uiEvent.postValue(event)

}