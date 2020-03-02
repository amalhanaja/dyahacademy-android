package com.amalcodes.dyahacademy.android.features.lesson

import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity

/**
 * @author: AMAL
 * Created On : 2020-02-22
 */


sealed class LessonListUIState : UIState.Abstract() {
    data class Content(val lessons: List<ItemEntity>) : LessonListUIState()
    data class GoToQuiz(
        val stateToRestore: UIState?,
        val lessonViewEntity: LessonViewEntity
    ) : LessonListUIState()

    data class GoToYoutube(
        val stateToRestore: UIState?,
        val lessonViewEntity: LessonViewEntity
    ) : LessonListUIState()
}