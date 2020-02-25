package com.amalcodes.dyahacademy.android.features.course

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


sealed class CourseDetailUIState {
    object Initial : CourseDetailUIState()
    object Loading : CourseDetailUIState()
    data class HasData(val data: CourseDetailViewEntity) : CourseDetailUIState()
    data class Error(val throwable: Throwable) : CourseDetailUIState()
}