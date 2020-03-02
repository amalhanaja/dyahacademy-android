package com.amalcodes.dyahacademy.android.data.repository.mapper

import com.amalcodes.dyahacademy.android.GetTopicByIdQuery
import com.amalcodes.dyahacademy.android.domain.model.Lesson
import com.amalcodes.dyahacademy.android.domain.model.LessonType

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


fun GetTopicByIdQuery.Lesson.toLessons(): List<Lesson> = listOf(
    Lesson(
        id = id(),
        title = title(),
        type = LessonType.valueOf(lessonType().rawValue())
    )
) + lessons().orEmpty().map { it.toLesson() }

fun GetTopicByIdQuery.Lesson1.toLesson(): Lesson = Lesson(
    id = id(),
    title = title(),
    type = LessonType.valueOf(lessonType().rawValue())
)