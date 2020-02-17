package com.amalcodes.dyahacademy.android.features.quiz

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


data class QuizViewEntity(
    val question: String,
    val answers: List<AnswerViewEntity>,
    val current: Int = 0,
    val count: Int
)