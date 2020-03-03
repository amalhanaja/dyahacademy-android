package com.amalcodes.dyahacademy.android.features.quiz

import android.graphics.Rect
import android.view.View
import androidx.core.view.isGone
import coil.api.load
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.Injector
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemQuestionBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 03/03/20
 */


class QuestionViewHolder(
    view: View
) : BaseViewHolder<QuestionViewEntity>(view), ViewBindingUninbder {

    private var binding: ItemQuestionBinding? = null

    private val adapter: MultiAdapter by lazy { MultiAdapter() }

    override fun onBind(entity: QuestionViewEntity) = ItemQuestionBinding.bind(itemView).run {
        binding = this
        Injector.markwon.setMarkdown(mtvQuizQuestion, entity.question)
        ivQuizQuestion.isGone = entity.questionImageUrl.isNullOrEmpty()
        if (!entity.questionImageUrl.isNullOrEmpty()) ivQuizQuestion.load(entity.questionImageUrl)
        rvAnswerSelection.adapter = adapter
        adapter.submitList(entity.answerSelections.map {
            it.copy(isSelected = it.answerMark == entity.answer)
        })
        rvAnswerSelection.isNestedScrollingEnabled = false
        if (rvAnswerSelection.itemDecorationCount == 0) {
            rvAnswerSelection.addItemDecoration(ItemOffsetDecoration { viewHolder, count ->
                val position = viewHolder.adapterPosition
                when (viewHolder) {
                    is AnswerSelectionViewHolder -> Rect().apply {
                        left = root.resources.getDimensionPixelSize(R.dimen.spacing_6)
                        right = root.resources.getDimensionPixelSize(R.dimen.spacing_6)
                        top = when (position) {
                            0 -> root.resources.getDimensionPixelSize(R.dimen.spacing_4)
                            else -> root.resources.getDimensionPixelSize(R.dimen.spacing_1)
                        }
                        bottom = when (position) {
                            count - 1 -> root.resources.getDimensionPixelSize(R.dimen.spacing_4)
                            else -> root.resources.getDimensionPixelSize(R.dimen.spacing_1)
                        }
                    }
                    else -> Rect()
                }
            })
        }
    }

    override fun onBindListener(entity: QuestionViewEntity, listener: ViewHolderClickListener) {
        adapter.setOnViewHolderClickListener(listener)
    }

    override fun unbind() {
        binding = null
    }
}