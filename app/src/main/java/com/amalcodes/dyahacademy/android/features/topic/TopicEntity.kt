package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.dyahacademy.android.features.lesson.LessonEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


@JsonClass(generateAdapter = true)
data class TopicEntity(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "lessons")
    val lessons: List<LessonEntity>? = null
)