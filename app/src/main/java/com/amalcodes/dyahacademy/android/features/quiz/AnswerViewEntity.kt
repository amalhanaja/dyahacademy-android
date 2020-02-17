package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


data class AnswerViewEntity(
    val text: String,
    val isSelected: Boolean = false,
    val isCorrect: Boolean = false
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_answer
}