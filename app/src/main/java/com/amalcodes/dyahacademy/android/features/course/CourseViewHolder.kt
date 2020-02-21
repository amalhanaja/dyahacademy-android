package com.amalcodes.dyahacademy.android.features.course

import android.view.View
import coil.api.load
import coil.transform.CircleCropTransformation
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_course.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


class CourseViewHolder(view: View) : BaseViewHolder<CourseViewEntity>(view) {
    override fun onBind(entity: CourseViewEntity) = itemView.run {
        actv_item_course_title?.text = entity.title
        actv_item_course_owner?.text = "Oleh: ${entity.createdBy}"
        val url =
            "https://lh3.googleusercontent.com/proxy/5ARsXZWaQOuV4s9O_5j-6XeMEmmn4jtGlIjc79dfb0_bkCZdyMoplCtQM7G2GrNHL9gdYusmmLh4tEzWRA5aqOMrFXrLl5LJu9pDnc_eGTt7rG21Q2jRFMemkqB0XHmUD5O5rfn9"
        siv_item_course?.load(url) {
            transformations(CircleCropTransformation())
        }
        Unit
    }

    override fun onBindListener(
        entity: CourseViewEntity,
        listener: ViewHolderClickListener
    ) = itemView.run {
        cl_item_course_wrapper?.setOnClickListener {
            listener.onClick(it, entity)
        }
        Unit
    }
}