package com.amalcodes.dyahacademy.android.features.quiz

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


data class QuizViewEntity(
    val question: String,
    val questionImageUrl: String?,
    val answerSelections: List<AnswerSelectionViewEntity>,
    var answer: String = "",
    val currentIndex: Int = 0,
    val count: Int
)