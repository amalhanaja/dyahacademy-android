package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.eventProperties
import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class QuizUIEvent : UIEvent.Abstract() {
    data class Fetch(
        val lessonId: String,
        val initialAnswer: List<AnswerViewEntity>? = null
    ) : QuizUIEvent(), Event {
        override val name: String = "fetch_quiz"
        override val properties: Map<String, Any?> = mapOf(
            "quiz_lesson_id" to lessonId,
            "is_correction_enabled" to !initialAnswer.isNullOrEmpty()
        )
    }

    data class FillAnswer(val answer: String, val position: Int) : QuizUIEvent(), Event {
        override val name: String = "fill_answer"
        override val properties: Map<String, Any?> = mapOf(
            "position" to position,
            "answer" to answer
        )
    }

    data class SetCurrentItem(val position: Int) : QuizUIEvent(), Event {
        override val name: String = "quiz_set_current_item"
        override val properties: Map<String, Any?> = mapOf(
            "position" to position
        )
    }

    object Finish : QuizUIEvent(), Event {
        override val name: String = "quiz_finish"
    }

    data class RetryFailure(
        val lessonId: String,
        val initialAnswer: List<AnswerViewEntity>?,
        val failure: Failure
    ) : QuizUIEvent(), Event {
        override val name: String = "retry_fetch_quiz"
        override val properties: Map<String, Any?> = failure.eventProperties() + mapOf(
            "quiz_lesson_id" to lessonId,
            "is_correction_enabled" to !initialAnswer.isNullOrEmpty()
        )
    }

}