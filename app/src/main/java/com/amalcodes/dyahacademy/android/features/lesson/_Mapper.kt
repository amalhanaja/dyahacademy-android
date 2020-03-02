package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.domain.model.Lesson

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


fun Lesson.toLessonViewEntity(): LessonViewEntity = LessonViewEntity(
    id = id,
    title = title,
    type = type,
    youtubeUrl = youtubeUrl
)