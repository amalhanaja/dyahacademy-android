package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.dyahacademy.android.core.UIState

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


sealed class TopicListUIState : UIState.Abstract() {
    data class Content(val topics: List<TopicViewEntity>) : TopicListUIState()
    data class GoToLessons(
        val stateToRestore: UIState?,
        val topicViewEntity: TopicViewEntity
    ) : TopicListUIState()
}