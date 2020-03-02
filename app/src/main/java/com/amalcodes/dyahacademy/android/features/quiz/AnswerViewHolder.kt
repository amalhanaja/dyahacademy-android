package com.amalcodes.dyahacademy.android.features.quiz

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemAnswerBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


class AnswerViewHolder(view: View) : BaseViewHolder<AnswerViewEntity>(view), ViewBindingUninbder {
    private var binding: ItemAnswerBinding? = null
    override fun onBind(entity: AnswerViewEntity) = ItemAnswerBinding.bind(itemView).run {
        binding = this
        val number = adapterPosition + 1
        mtvItemAnswerMark.text = entity.answer
        mtvItemAnswerNumber.text = number.toString()
        mtvItemAnswerNumber.backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(root.context.resources, entity.numberBackgroundTint, null)
        )
        mtvItemAnswerNumber.setTextColor(
            ResourcesCompat.getColor(root.context.resources, entity.numberTextColor, null)
        )
        mtvItemAnswerMark.setTextColor(
            ResourcesCompat.getColor(root.context.resources, entity.answerTextColor, null)
        )
        Unit
    }

    override fun onBindListener(
        entity: AnswerViewEntity,
        listener: ViewHolderClickListener
    ) = requireNotNull(binding).run {
        llAnswer.setOnClickListener { listener.onClick(it, entity) }
    }

    override fun unbind() {
        binding = null
    }
}