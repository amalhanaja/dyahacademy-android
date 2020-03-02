package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.core.UIState

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


sealed class CourseListUIState : UIState.Abstract() {
    data class Content(val list: List<CourseViewEntity>) : CourseListUIState()
    data class GoToTopics(
        val stateToRestore: UIState?,
        val courseViewEntity: CourseViewEntity
    ) : CourseListUIState()
}