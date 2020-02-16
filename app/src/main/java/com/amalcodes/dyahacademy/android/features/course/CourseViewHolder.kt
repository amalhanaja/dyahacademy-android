package com.amalcodes.dyahacademy.android.features.course

import android.view.View
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