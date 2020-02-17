package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.R
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


sealed class LessonViewEntity : ItemEntity {
    abstract val id: String
    abstract val title: String
    abstract val nextLesson: LessonViewEntity?
    abstract val prevLesson: LessonViewEntity?

    override val layoutRes: Int
        get() = R.layout.item_lesson_default

    data class Youtube(
        override val id: String,
        override val title: String,
        override val nextLesson: LessonViewEntity? = null,
        override val prevLesson: LessonViewEntity? = null,
        val youtubeUrl: String
    ) : LessonViewEntity()

    data class Markdown(
        override val id: String,
        override val title: String,
        override val nextLesson: LessonViewEntity? = null,
        override val prevLesson: LessonViewEntity? = null,
        val content: String
    ) : LessonViewEntity()

    data class Document(
        override val id: String,
        override val title: String,
        override val nextLesson: LessonViewEntity? = null,
        override val prevLesson: LessonViewEntity? = null,
        val url: String
    ) : LessonViewEntity()

    data class Quiz(
        override val id: String,
        override val title: String,
        override val nextLesson: LessonViewEntity? = null,
        override val prevLesson: LessonViewEntity? = null
    ) : LessonViewEntity()

}