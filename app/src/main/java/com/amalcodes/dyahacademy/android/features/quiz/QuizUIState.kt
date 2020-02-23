package com.amalcodes.dyahacademy.android.features.quiz

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


sealed class QuizUIState {
    object Initial : QuizUIState()
    data class HasData(
        val data: QuizViewEntity,
        val answers: List<AnswerViewEntity>
    ) : QuizUIState()
    data class AnswerFilled(val answers: List<AnswerViewEntity>) : QuizUIState()
    data class QuizFinished(
        val correctAnswer: Int,
        val incorrectAnswer: Int,
        val blankAnswer: Int,
        val score: Int
    ) : QuizUIState()
    data class Error(val throwable: Throwable) : QuizUIState()
}