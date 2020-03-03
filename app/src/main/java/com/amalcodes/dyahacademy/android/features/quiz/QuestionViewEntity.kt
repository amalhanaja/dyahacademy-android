package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


data class QuestionViewEntity(
    val question: String,
    val questionImageUrl: String?,
    val answerSelections: List<AnswerSelectionViewEntity>,
    var answer: String = ""
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_question
}