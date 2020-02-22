package com.amalcodes.dyahacademy.android.features.quiz

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


data class AnswerSelectionViewEntity(
    val text: String,
    val isSelected: Boolean = false,
    val isCorrect: Boolean = false,
    val answerMark: Char
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_answer_selection

    val markerBackgroundRes: Int
        @DrawableRes
        get() = when (isSelected) {
            true -> R.drawable.shape_round_white_xl
            false -> 0
        }

    val markerBackgroundTint: Int?
        @ColorRes
        get() = when {
            isSelected -> R.color.coral
            else -> null
        }

    val markerTextColor: Int
        @ColorRes
        get() = when {
            isSelected -> R.color.white
            else -> R.color.coral
        }
}