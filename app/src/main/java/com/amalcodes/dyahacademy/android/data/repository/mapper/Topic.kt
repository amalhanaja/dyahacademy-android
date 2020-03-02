package com.amalcodes.dyahacademy.android.data.repository.mapper

import com.amalcodes.dyahacademy.android.GetCourseByIdQuery
import com.amalcodes.dyahacademy.android.domain.model.Topic

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


fun GetCourseByIdQuery.Topic.toTopic() = Topic(
    id = id(),
    title = title()
)