package com.tapi.android.example.fragment.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class GridItemDecorator(val context: Context, private val spacingPx: Int, private val mGridSize: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val bit = if (spacingPx > mGridSize) (spacingPx / mGridSize) else 1
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        outRect.top = if (itemPosition < mGridSize) 0 else (bit * mGridSize)
        outRect.bottom = 0

        val rowPosition = itemPosition % mGridSize
        outRect.left = (rowPosition * bit)
        outRect.right = ((mGridSize - rowPosition - 1) * bit)

    }
}