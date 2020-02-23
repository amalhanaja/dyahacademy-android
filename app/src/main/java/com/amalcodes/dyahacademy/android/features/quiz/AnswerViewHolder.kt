package com.amalcodes.dyahacademy.android.features.quiz

import android.content.res.ColorStateList
import android.view.View
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_answer.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


class AnswerViewHolder(view: View) : BaseViewHolder<AnswerViewEntity>(view) {
    override fun onBind(entity: AnswerViewEntity) = itemView.run {
        val number = adapterPosition + 1
        mtv_item_answer_mark?.text = entity.answer
        mtv_item_answer_number?.text = number.toString()
        mtv_item_answer_number?.backgroundTintList = ColorStateList.valueOf(
            when {
                entity.isCurrent -> context.resources.getColor(R.color.coral)
                else -> context.resources.getColor(R.color.white)
            }
        )
        mtv_item_answer_number?.setTextColor(
            when {
                entity.isCurrent -> context.resources.getColor(R.color.white)
                else -> context.resources.getColor(R.color.coral)
            }
        )
        Unit
    }

    override fun onBindListener(
        entity: AnswerViewEntity,
        listener: ViewHolderClickListener
    ) = itemView.run {
        ll_answer?.setOnClickListener {
            listener.onClick(it, entity)
        }
        Unit
    }
}