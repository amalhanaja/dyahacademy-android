package com.amalcodes.dyahacademy.android.core

import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class UIEvent {
    data class RetryFailure(val failure: Failure) : UIEvent(), Event {
        override val name: String = "retry"
        override val properties: Map<String, Any?> = mapOf(
            "failure" to failure.message,
            "caused" to failure.cause?.message
        )
    }

    data class RestoreUIState(val uiState: UIState?) : UIEvent()

    abstract class Abstract : UIEvent()

    fun unhandled(): Nothing {
        throw IllegalStateException("Unhandled Event: $this")
    }
}