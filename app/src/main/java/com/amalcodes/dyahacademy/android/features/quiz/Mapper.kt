package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.domain.mapIfNotEmpty
import com.amalcodes.dyahacademy.android.domain.model.Answer
import com.amalcodes.dyahacademy.android.domain.model.Quiz

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


fun Quiz.toQuizViewEntity(answer: String, index: Int, showCorrection: Boolean): QuizViewEntity =
    QuizViewEntity(
        question = question,
        questionImageUrl = questionImageUrl,
        answer = answer,
        currentIndex = index,
        answerSelections = answers.mapIfNotEmpty { answerItem ->
            answerItem.toAnswerSelectionViewEntity(
                isSelected = answer == answerItem.mark,
                mark = answerItem.mark,
                showCorrection = showCorrection
            )
        }
    )

fun Answer.toAnswerSelectionViewEntity(
    isSelected: Boolean,
    mark: String,
    showCorrection: Boolean
): AnswerSelectionViewEntity = AnswerSelectionViewEntity(
    text = text,
    isSelected = isSelected,
    isCorrect = isCorrect,
    answerMark = mark,
    isCorrectionEnabled = showCorrection
)