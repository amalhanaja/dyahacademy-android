package com.amalcodes.dyahacademy.android.features.topic

import android.view.View
import com.amalcodes.dyahacademy.android.core.ViewBindingUninbder
import com.amalcodes.dyahacademy.android.databinding.ItemTopicBinding
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder
import com.amalcodes.ezrecyclerview.adapter.viewholder.ViewHolderClickListener

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


class TopicViewHolder(view: View) : BaseViewHolder<TopicViewEntity>(view), ViewBindingUninbder {

    private var binding: ItemTopicBinding? = null

    override fun onBind(entity: TopicViewEntity) = ItemTopicBinding.bind(itemView).run {
        binding = this
        tvItemTopicTitle.text = entity.title
    }

    override fun onBindListener(
        entity: TopicViewEntity,
        listener: ViewHolderClickListener
    ) = binding?.run {
        flItemTopic.setOnClickListener { listener.onClick(it, entity) }
    }.let { Unit }

    override fun unbind() {
        binding = null
    }
}