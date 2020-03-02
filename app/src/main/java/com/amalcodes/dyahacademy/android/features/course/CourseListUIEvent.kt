package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.core.UIEvent

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


sealed class CourseListUIEvent : UIEvent.Abstract() {

    object FetchCourses : CourseListUIEvent(), Event {
        override val name: String = "fetch_courses"
    }
}