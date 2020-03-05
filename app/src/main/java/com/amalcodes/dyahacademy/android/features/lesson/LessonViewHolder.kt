package com.amalcodes.dyahacademy.android.features.lesson

import android.view.View
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemLessonBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


class LessonViewHolder(view: View) : BaseViewHolder<LessonViewEntity>(view), ViewBindingUninbder {

    private var binding: ItemLessonBinding? = null

    override fun onBind(entity: LessonViewEntity) = ItemLessonBinding.bind(itemView).run {
        binding = this
        actvItemLessonTitle.text = entity.title
        ivItemLessonType.setImageResource(entity.typeIconRes)
        actvItemLessonType.text = root.context.getString(entity.typeNameStringRes)
    }

    override fun onBindListener(
        entity: LessonViewEntity,
        listener: ViewHolderClickListener
    ) = binding?.run {
        clItemLesson.setOnClickListener { listener.onClick(it, entity) }
    }.let { Unit }

    override fun unbind() {
        binding = null
    }
}