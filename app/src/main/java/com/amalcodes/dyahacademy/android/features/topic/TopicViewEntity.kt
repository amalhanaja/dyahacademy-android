package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewEntity
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


data class TopicViewEntity(
    val id: String,
    val title: String,
    val description: String,
    val lessons: List<LessonViewEntity> = emptyList()
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_topic
}