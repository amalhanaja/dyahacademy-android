package com.amalcodes.dyahacademy.android.data.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerResponse(
    @Json(name = "text")
    val text: String? = null,
    @Json(name = "isCorrect")
    val isCorrect: Boolean? = null
)
