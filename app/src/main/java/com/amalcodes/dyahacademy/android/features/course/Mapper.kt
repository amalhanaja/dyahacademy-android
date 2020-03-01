package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.domain.model.Course

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


fun Course.toCourseViewEntity(): CourseViewEntity = CourseViewEntity(this)