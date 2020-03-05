package com.amalcodes.dyahacademy.android.features.course

import android.view.View
import coil.api.load
import coil.transform.CircleCropTransformation
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemCourseBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


class CourseViewHolder(view: View) : BaseViewHolder<CourseViewEntity>(view), ViewBindingUninbder {

    private var binding: ItemCourseBinding? = null

    override fun onBind(entity: CourseViewEntity) = ItemCourseBinding.bind(itemView).run {
        binding = this
        actvItemCourseTitle.text = entity.course.title
        actvItemCourseOwner.text =
            root.context.getString(R.string.text_By_colon, entity.course.creator)
        sivItemCourse.load(entity.course.thumbnailUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }.let { Unit }

    override fun onBindListener(
        entity: CourseViewEntity,
        listener: ViewHolderClickListener
    ) = binding?.run {
        clItemCourseWrapper.setOnClickListener { listener.onClick(it, entity) }
    }.let { Unit }

    override fun unbind() {
        binding = null
    }
}