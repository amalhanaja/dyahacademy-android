package com.amalcodes.dyahacademy.android.features.lesson

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.domain.model.LessonType
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


data class LessonViewEntity(
    val id: String,
    val title: String,
    val type: LessonType,
    val youtubeUrl: String? = null
) : ItemEntity {
    override val layoutRes: Int
        get() = when (type) {
            LessonType.LESSON_GROUP -> R.layout.item_lesson_group_title
            else -> R.layout.item_lesson
        }

    val typeIconRes: Int
        @DrawableRes
        get() = when (type) {
            LessonType.YOUTUBE -> R.drawable.ic_youtube
            LessonType.QUIZ, LessonType.TEST -> R.drawable.ic_help_circle
            else -> throw IllegalStateException("Unexpected LessonType: $type")
        }

    val typeNameStringRes: Int
        @StringRes
        get() = when (type) {
            LessonType.QUIZ -> R.string.text_Quiz
            LessonType.YOUTUBE -> R.string.text_Video
            LessonType.TEST -> R.string.text_Test
            else -> throw IllegalStateException("Unexpected LessonType: $type")
        }

}

fun LessonViewEntity.eventProperties(): Map<String, Any?> = mapOf(
    "lesson_id" to id,
    "lesson_title" to title,
    "lesson_type" to type,
    "lesson_youtube_url" to youtubeUrl
)