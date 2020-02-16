package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


data class CourseViewEntity(
    val id: String,
    val title: String,
    val createdBy: String
) : ItemEntity {
    override val layoutRes: Int
        get() = R.layout.item_course
}