package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.core.Injector
import kotlinx.coroutines.flow.Flow
import retrofit2.create

/**
 * @author: AMAL
 * Created On : 2020-02-17
 */

object LessonRepository {

    private val lessonAPI: LessonAPI by lazy {
        Injector.retrofit.create<LessonAPI>()
    }

    fun getLessonById(lessonId: String): Flow<LessonEntity> {
        return lessonAPI.getLessonById(lessonId)
    }
}