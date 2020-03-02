package com.amalcodes.dyahacademy.android.domain.model

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


data class Quiz(
    val id: String,
    val question: String,
    val questionImageUrl: String,
    val answer: String,
    val answers: List<Answer>
)