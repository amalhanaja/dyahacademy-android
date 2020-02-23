package com.amalcodes.dyahacademy.android.features.quiz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: AMAL
 * Created On : 2020-02-24
 */


@Parcelize
data class QuizSummaryViewEntity(
    val score: Int,
    val correctAnswer: Int,
    val wrongAnswer: Int,
    val blankAnswer: Int
) : Parcelable