package com.amalcodes.dyahacademy.android.features.quiz

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerEntity(
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "isCorrect")
    val isCorrect: Boolean? = null
)
