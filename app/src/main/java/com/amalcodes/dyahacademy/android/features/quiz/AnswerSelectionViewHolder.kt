package com.amalcodes.dyahacademy.android.features.quiz

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemAnswerSelectionBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


class AnswerSelectionViewHolder(
    view: View
) : BaseViewHolder<AnswerSelectionViewEntity>(view), ViewBindingUninbder {

    private var binding: ItemAnswerSelectionBinding? = null

    override fun onBind(
        entity: AnswerSelectionViewEntity
    ) = ItemAnswerSelectionBinding.bind(itemView).run {
        binding = this
        mtvItemAnswerSelectionText.text = entity.text
        mtvItemAnswerSelectionMark.text = entity.answerMark
        mtvItemAnswerSelectionMark.setBackgroundResource(entity.markerBackgroundRes)
        mtvItemAnswerSelectionMark.backgroundTintList = entity.markerBackgroundTint?.let {
            ColorStateList.valueOf(ResourcesCompat.getColor(root.context.resources, it, null))
        }
        clItemAnswer.backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(root.context.resources, entity.backgroundTint, null)
        )
        mtvItemAnswerSelectionText.setTextColor(
            ResourcesCompat.getColor(root.context.resources, entity.textColor, null)
        )
        mtvItemAnswerSelectionMark.setTextColor(
            ResourcesCompat.getColor(root.context.resources, entity.markerTextColor, null)
        )
    }

    override fun onBindListener(
        entity: AnswerSelectionViewEntity,
        listener: ViewHolderClickListener
    ) = binding?.run {
        clItemAnswer.setOnClickListener { buttonView -> listener.onClick(buttonView, entity) }
    }.let { Unit }

    override fun unbind() {
        binding = null
    }
}