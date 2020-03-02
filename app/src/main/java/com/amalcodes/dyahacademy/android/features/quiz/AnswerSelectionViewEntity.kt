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
    val isSelected: Boolean,
    val isCorrect: Boolean,
    val answerMark: String,
    val isCorrectionEnabled: Boolean = false
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
            isSelected or isCorrectionEnabled -> R.color.white
            else -> R.color.coral
        }

    val backgroundTint: Int
        @ColorRes
        get() = when {
            isCorrectionEnabled and isCorrect -> R.color.dodgerBlue
            isCorrectionEnabled and !isCorrect -> R.color.radicalRed
            else -> R.color.white
        }

    val textColor: Int
        @ColorRes
        get() = when {
            isCorrectionEnabled -> R.color.white
            else -> R.color.jet
        }
}