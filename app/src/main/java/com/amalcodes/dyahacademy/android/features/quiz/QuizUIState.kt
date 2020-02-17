package com.amalcodes.dyahacademy.android.features.quiz

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


sealed class QuizUIState {
    object Initial : QuizUIState()
    data class HasData(val data: QuizViewEntity) : QuizUIState()
    data class Error(val throwable: Throwable) : QuizUIState()
}