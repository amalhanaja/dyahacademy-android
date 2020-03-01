package com.amalcodes.dyahacademy.android.data.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author: AMAL
 * Created On : 26/02/20
 */

@JsonClass(generateAdapter = true)
data class LessonResponse(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "lessonType")
    val lessonType: String? = null,
    @Json(name = "quizzes")
    val quizzes: List<QuizResponse>? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "youtubeUrl")
    val youtubeUrl: String? = null
)