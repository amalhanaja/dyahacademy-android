package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


data class AnswerViewEntity(
    val id: Int,
    val answer: String,
    val isCurrent: Boolean
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_answer

}