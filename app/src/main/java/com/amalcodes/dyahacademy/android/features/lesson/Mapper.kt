package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.core.Injector
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


fun LessonEntity.flatten(): List<LessonEntity> {
    val subLesson = lessons.orEmpty().map {
        val adapter = LessonEntityJsonAdapter(Injector.moshi)
        Timber.d("${adapter.fromJsonValue(it)}")
    }
    return listOf(this)
}

fun LessonEntity.toLessonViewEntity(): LessonViewEntity = LessonViewEntity(
    id = requireNotNull(id),
    title = requireNotNull(title),
    type = LessonType.valueOf(requireNotNull(lessonType)),
    youtubeUrl = youtubeUrl
)