package com.amalcodes.dyahacademy.android.features.topic

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


interface TopicAPI {

    @GET("/topics/{id}")
    fun getTopicById(@Path("id") id: String): Flow<TopicEntity>
}