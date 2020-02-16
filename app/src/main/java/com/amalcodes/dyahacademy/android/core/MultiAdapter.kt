package com.amalcodes.dyahacademy.android.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.features.course.CourseViewHolder
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewHolder
import com.amalcodes.dyahacademy.android.features.topic.TopicViewHolder
import com.amalcodes.ezrecyclerview.adapter.BaseAdapter
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-14
 */


class MultiAdapter(data: MutableList<ItemEntity> = mutableListOf()) :
    BaseAdapter<ItemEntity>(data) {
    override fun onCreateBaseViewHolder(view: View, layoutRes: Int): RecyclerView.ViewHolder {
        return when (layoutRes) {
            R.layout.item_course -> CourseViewHolder(view)
            R.layout.item_topic -> TopicViewHolder(view)
            R.layout.item_lesson_default -> LessonViewHolder(view)
            else -> throw IllegalStateException("unexpected View Holder for layoutRes: $layoutRes")
        }
    }
}