package com.amalcodes.dyahacademy.android.features.lesson

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_lesson_default.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


class LessonViewHolder(view: View) : BaseViewHolder<LessonViewEntity>(view) {

    override fun onBind(entity: LessonViewEntity) = itemView.run {
        actv_item_lesson_default_title?.text = entity.title
    }

    override fun onBindListener(
        entity: LessonViewEntity,
        listener: ViewHolderClickListener
    ) = itemView.run {
        cl_item_lesson_default_wrapper?.setOnClickListener {
            listener.onClick(it, entity)
        }
        Unit
    }
}