package com.amalcodes.dyahacademy.android.features.lesson

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


interface LessonAPI {
    @GET("/lessons/{id}")
    fun getLessonById(@Path("id") id: String): Flow<LessonEntity>
}