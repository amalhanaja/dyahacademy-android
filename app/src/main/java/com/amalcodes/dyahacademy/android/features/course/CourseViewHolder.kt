package com.amalcodes.dyahacademy.android.features.course

import android.view.View
import coil.api.load
import coil.transform.CircleCropTransformation
import com.amalcodes.dyahacademy.android.R
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
        actv_item_course_owner?.text = context.getString(R.string.text_By_colon, entity.createdBy)
        val url =
            "http://sim.labschoolcibubur.sch.id/web/image/labschool.matapelajaran/523/image"
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