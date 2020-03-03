package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.eventProperties
import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class TopicListUIEvent : UIEvent.Abstract() {
    data class Fetch(val courseId: String) : TopicListUIEvent(), Event {
        override val name: String = "fetch_topics"
        override val properties: Map<String, Any?> = mapOf(
            "course_id" to courseId
        )
    }

    data class Refresh(val courseId: String) : TopicListUIEvent(), Event {
        override val name: String = "refresh_topics"
        override val properties: Map<String, Any?> = mapOf(
            "course_id" to courseId
        )
    }

    data class RetryFailure(val courseId: String, val failure: Failure) : TopicListUIEvent(),
        Event {
        override val name: String = "retry_fetch_topics"
        override val properties: Map<String, Any?> = failure.eventProperties() + mapOf(
            "course_id" to courseId
        )
    }

    data class GoToLessons(val topicViewEntity: TopicViewEntity) : TopicListUIEvent(), Event {
        override val name: String = "go_to_lessons"
        override val properties: Map<String, Any?> = mapOf(
            "topic_id" to topicViewEntity.id,
            "topic_title" to topicViewEntity.title
        )
    }
}