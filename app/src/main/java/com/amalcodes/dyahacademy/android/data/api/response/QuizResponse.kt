package com.amalcodes.dyahacademy.android.data.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


@JsonClass(generateAdapter = true)
data class QuizResponse(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "question")
    val question: String? = null,
    @Json(name = "questionImageUrl")
    val questionImageUrl: String? = null,
    @Json(name = "answers")
    val answers: List<AnswerResponse>? = null
)