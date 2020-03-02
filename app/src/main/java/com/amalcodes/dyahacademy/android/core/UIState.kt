package com.amalcodes.dyahacademy.android.core

import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


sealed class UIState {
    object Initial : UIState()
    object Loading : UIState()
    data class Failed(val failure: Failure) : UIState()
    abstract class Abstract : UIState()

    var hasBeenHandled = false
        private set

}