package com.amalcodes.dyahacademy.android.data.api

import com.amalcodes.dyahacademy.android.data.api.response.LessonResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


interface DyahAcademyApi {
    @GET("/lessons/{id}")
    fun getLesson(@Path("id") id: String): Flow<LessonResponse>
}