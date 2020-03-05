package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.eventProperties
import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class CourseListUIEvent : UIEvent.Abstract() {

    object FetchCourses : CourseListUIEvent(), Event {
        override val name: String = "fetch_courses"
    }

    object Refresh : CourseListUIEvent(), Event {
        override val name: String = "refresh_courses"
    }

    data class RetryFailure(val failure: Failure) : CourseListUIEvent(), Event {
        override val name: String = "retry_fetch_course"
        override val properties: Map<String, Any?> = failure.eventProperties()
    }

    data class GoToTopics(val courseViewEntity: CourseViewEntity) : CourseListUIEvent(), Event {
        override val name: String = "go_to_topics"
        override val properties: Map<String, Any?>
            get() = mapOf(
                "course_title" to courseViewEntity.course.title,
                "course_id" to courseViewEntity.course.id,
                "creator" to courseViewEntity.course.title
            )
    }
}