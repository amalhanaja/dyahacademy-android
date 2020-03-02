package com.amalcodes.dyahacademy.android.domain.model

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


data class Lesson(
    val id: String,
    val title: String,
    val type: LessonType,
    val youtubeUrl: String? = null
)