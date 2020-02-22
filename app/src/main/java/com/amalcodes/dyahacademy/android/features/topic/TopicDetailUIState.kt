package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


sealed class TopicDetailUIState {
    object Initial : TopicDetailUIState()
    data class Error(val throwable: Throwable) : TopicDetailUIState()
    data class HasData(val data: List<ItemEntity>) : TopicDetailUIState()
}