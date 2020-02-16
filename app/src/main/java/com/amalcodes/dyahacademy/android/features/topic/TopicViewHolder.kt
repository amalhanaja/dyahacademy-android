package com.amalcodes.dyahacademy.android.features.topic

import android.graphics.Rect
import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_topic.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


class TopicViewHolder(view: View) : BaseViewHolder<TopicViewEntity>(view) {

    private val adapter by lazy {
        MultiAdapter()
    }

    override fun onBind(entity: TopicViewEntity) = itemView.run {
        rv_item_topic_lessons?.adapter = adapter
        if (rv_item_topic_lessons?.itemDecorationCount == 0) {
            rv_item_topic_lessons?.addItemDecoration(
                ItemOffsetDecoration { viewHolder: RecyclerView.ViewHolder, count: Int ->
                    val position = viewHolder.adapterPosition
                    if (viewHolder is LessonViewHolder) {
                        return@ItemOffsetDecoration Rect().apply {
                            left = resources.getDimensionPixelSize(R.dimen.spacing_1)
                            right = resources.getDimensionPixelSize(R.dimen.spacing_1)
                            top = when (position) {
                                0 -> resources.getDimensionPixelSize(R.dimen.spacing_2)
                                else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                            }
                            bottom = when (position) {
                                count - 1 -> resources.getDimensionPixelSize(R.dimen.spacing_2)
                                else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                            }
                        }
                    }
                    return@ItemOffsetDecoration Rect()
                }
            )
        }
        tv_item_topic_title?.text = entity.title
        tv_item_topic_description?.text = entity.description
        tv_item_topic_description?.isGone = entity.description.isEmpty()
        adapter.submitList(entity.lessons)
    }

    override fun onBindListener(entity: TopicViewEntity, listener: ViewHolderClickListener) {
        adapter.setOnViewHolderClickListener(listener)
    }
}