package com.amalcodes.dyahacademy.android.features.quiz

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.Keep
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import kotlinx.android.parcel.Parcelize

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


@Keep
@Parcelize
data class AnswerViewEntity(
    val id: Int,
    val answer: String,
    val isCurrent: Boolean,
    val isCorrect: Boolean,
    val isCorrectionEnabled: Boolean
) : ItemEntity, Parcelable {

    override val layoutRes: Int
        get() = R.layout.item_answer

    val numberBackgroundTint: Int
        @ColorRes
        get() = when {
            isCurrent -> R.color.coral
            else -> R.color.white
        }

    val numberTextColor: Int
        @ColorRes
        get() = when {
            isCurrent -> R.color.white
            else -> R.color.coral
        }

    val answerTextColor: Int
        @ColorRes
        get() = when {
            isCorrectionEnabled && isCorrect -> R.color.dodgerBlue
            isCorrectionEnabled && !isCorrect -> R.color.radicalRed
            else -> R.color.outerSpace
        }

}