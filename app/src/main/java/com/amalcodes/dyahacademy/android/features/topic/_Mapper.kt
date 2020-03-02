package com.amalcodes.dyahacademy.android.features.topic

import com.amalcodes.dyahacademy.android.domain.model.Topic

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


fun Topic.toTopicViewEntity(): TopicViewEntity = TopicViewEntity(
    id = id,
    title = title
)