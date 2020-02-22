package com.amalcodes.dyahacademy.android.features.quiz

import android.content.res.ColorStateList
import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_answer_selection.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */


class AnswerSelectionViewHolder(view: View) : BaseViewHolder<AnswerSelectionViewEntity>(view) {
    override fun onBind(entity: AnswerSelectionViewEntity) = itemView.run {
        mtv_item_answer_selection_text?.text = entity.text
        mtv_item_answer_selection_mark?.text = entity.answerMark.toString()
        mtv_item_answer_selection_mark?.setBackgroundResource(entity.markerBackgroundRes)
        mtv_item_answer_selection_mark?.backgroundTintList = entity.markerBackgroundTint?.let {
            ColorStateList.valueOf(context.resources.getColor(it))
        }
        mtv_item_answer_selection_mark?.setTextColor(context.resources.getColor(entity.markerTextColor))
        Unit
    }

    override fun onBindListener(
        entity: AnswerSelectionViewEntity,
        listener: ViewHolderClickListener
    ) = itemView.run {
        cl_item_answer?.setOnClickListener { buttonView ->
            listener.onClick(buttonView, entity)
        }
        Unit
    }
}