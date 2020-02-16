package com.amalcodes.dyahacademy.android.core

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: AMAL
 * Created On : 2020-02-16
 */

class ItemOffsetDecoration(
    private val rectBuilder: (viewHolder: RecyclerView.ViewHolder, count: Int) -> Rect
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val count = state.itemCount
        val viewHolder = parent.getChildViewHolder(view)
        val rectOffset = rectBuilder.invoke(viewHolder, count)
        outRect.set(rectOffset)
    }
}
