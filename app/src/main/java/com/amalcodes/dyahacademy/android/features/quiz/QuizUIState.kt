package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.core.UIState

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


sealed class QuizUIState : UIState.Abstract() {
    data class Default(
        val quiz: QuizViewEntity,
        val answers: List<AnswerViewEntity>
    ) : QuizUIState()

    data class Finished(
        val summary: QuizSummaryViewEntity,
        val answers: List<AnswerViewEntity>
    ) : QuizUIState()

    data class Correction(
        val quiz: QuizViewEntity,
        val answers: List<AnswerViewEntity>
    ) : QuizUIState()
}