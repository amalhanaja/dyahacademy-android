package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.features.topic.TopicViewEntity

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


data class CourseDetailViewEntity(
    val title: String,
    val description: String,
    val topics: List<TopicViewEntity>
)