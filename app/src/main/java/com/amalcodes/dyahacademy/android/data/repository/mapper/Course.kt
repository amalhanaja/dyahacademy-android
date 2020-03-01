package com.amalcodes.dyahacademy.android.data.repository.mapper

import com.amalcodes.dyahacademy.android.GetAllCoursesQuery
import com.amalcodes.dyahacademy.android.domain.model.Course

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


fun GetAllCoursesQuery.Course.toCourse(): Course = Course(
    id = id(),
    title = title(),
    thumbnailUrl = thumbnailUrl(),
    creator = createdBy()
)