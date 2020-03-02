package com.amalcodes.dyahacademy.android.core

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class UIEvent {

    data class RestoreUIState(val uiState: UIState?) : UIEvent()

    abstract class Abstract : UIEvent()

    fun unhandled(): Nothing {
        throw IllegalStateException("Unhandled Event: $this")
    }
}