package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.eventProperties
import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class LessonListUIEvent : UIEvent.Abstract() {
    data class Fetch(val topicId: String) : LessonListUIEvent(), Event {
        override val name: String = "fetch_lessons"
        override val properties: Map<String, Any?> = mapOf(
            "topic_id" to topicId
        )
    }

    data class Refresh(val topicId: String) : LessonListUIEvent(), Event {
        override val name: String = "refresh_lessons"
        override val properties: Map<String, Any?> = mapOf(
            "topic_id" to topicId
        )
    }

    data class RetryFailure(val topicId: String, val failure: Failure) : LessonListUIEvent(),
        Event {
        override val name: String = "retry_fetch_lessons"
        override val properties: Map<String, Any?> = failure.eventProperties() + mapOf(
            "topic_id" to topicId
        )
    }

    data class GoToQuiz(val lessonViewEntity: LessonViewEntity) : LessonListUIEvent(), Event {
        override val name: String = "go_to_quiz"
        override val properties: Map<String, Any?> = lessonViewEntity.eventProperties()
    }

    data class GoToYoutube(val lessonViewEntity: LessonViewEntity) : LessonListUIEvent(), Event {
        override val name: String = "go_to_youtube"
        override val properties: Map<String, Any?> = lessonViewEntity.eventProperties()
    }
}