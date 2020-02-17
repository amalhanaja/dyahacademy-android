package com.amalcodes.dyahacademy.android.features.quiz

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_answer.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


class AnswerViewHolder(view: View) : BaseViewHolder<AnswerViewEntity>(view) {
    override fun onBind(entity: AnswerViewEntity) = itemView.run {
        mrb_item_answer?.text = entity.text
        mrb_item_answer?.isChecked = entity.isSelected
    }

    override fun onBindListener(
        entity: AnswerViewEntity,
        listener: ViewHolderClickListener
    ) = itemView.run {
        mrb_item_answer?.setOnClickListener { buttonView ->
            listener.onClick(buttonView, entity)
        }
        Unit
    }
}