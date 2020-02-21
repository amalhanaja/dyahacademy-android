package com.amalcodes.dyahacademy.android.features.topic

import android.view.View
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener
import kotlinx.android.synthetic.main.item_topic.view.*

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


class TopicViewHolder(view: View) : BaseViewHolder<TopicViewEntity>(view) {

    override fun onBind(entity: TopicViewEntity) = itemView.run {
        tv_item_topic_title?.text = entity.title
    }

    override fun onBindListener(
        entity: TopicViewEntity,
        listener: ViewHolderClickListener
    ) = itemView.run {
        fl_item_topic?.setOnClickListener {
            listener.onClick(it, entity)
        }
        Unit
    }

}