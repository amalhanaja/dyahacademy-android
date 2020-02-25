package com.amalcodes.dyahacademy.android.features.course

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


sealed class CourseListUIState {
    object Initial : CourseListUIState()
    object Loading : CourseListUIState()
    data class HasData(val data: List<CourseViewEntity>) : CourseListUIState()
    data class Error(val throwable: Throwable) : CourseListUIState()
}