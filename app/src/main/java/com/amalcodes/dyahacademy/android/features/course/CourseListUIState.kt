package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.core.UIState

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


data class CourseListUIState(val list: List<CourseViewEntity>) : UIState.Abstract()