package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.features.quiz.QuizEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author: AMAL
 * Created On : 26/02/20
 */

@JsonClass(generateAdapter = true)
data class LessonEntity(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "lessonType")
    val lessonType: String? = null,
    @Json(name = "quizzes")
    val quizzes: List<QuizEntity>? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "youtubeUrl")
    val youtubeUrl: String? = null,
    @Json(name = "lessons")
    val lessons: List<String>? = null
)