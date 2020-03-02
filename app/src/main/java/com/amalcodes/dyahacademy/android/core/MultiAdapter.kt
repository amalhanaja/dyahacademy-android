package com.amalcodes.dyahacademy.android.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.features.course.CourseViewHolder
import com.amalcodes.dyahacademy.android.features.lesson.LessonGroupTitleViewHolder
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewHolder
import com.amalcodes.dyahacademy.android.features.quiz.AnswerSelectionViewHolder
import com.amalcodes.dyahacademy.android.features.quiz.AnswerViewHolder
import com.amalcodes.dyahacademy.android.features.topic.TopicViewHolder
import com.amalcodes.ezrecyclerview.adapter.BaseAdapter
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.amalcodes.ezrecyclerview.adapter.viewholder.BaseViewHolder

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
            R.layout.item_lesson -> LessonViewHolder(view)
            R.layout.item_answer_selection -> AnswerSelectionViewHolder(view)
            R.layout.item_lesson_group_title -> LessonGroupTitleViewHolder(view)
            R.layout.item_answer -> AnswerViewHolder(view)
            else -> throw IllegalStateException("unexpected View Holder for layoutRes: $layoutRes")
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ItemEntity>) {
        if (holder is ViewBindingUninbder) {
            holder.unbind()
        }
    }
}