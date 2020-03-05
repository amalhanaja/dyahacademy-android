package com.amalcodes.dyahacademy.android.features.lesson

import android.view.View
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemLessonGroupTitleBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


class LessonGroupTitleViewHolder(
    view: View
) : BaseViewHolder<LessonViewEntity>(view), ViewBindingUninbder {

    private var binding: ItemLessonGroupTitleBinding? = null

    override fun onBind(entity: LessonViewEntity) = ItemLessonGroupTitleBinding.bind(itemView)
        .run {
            binding = this
            actvItemLessonGroupTitle.text = entity.title
        }

    override fun onBindListener(
        entity: LessonViewEntity,
        listener: ViewHolderClickListener
    ) = Unit

    override fun unbind() {
        binding = null
    }
}