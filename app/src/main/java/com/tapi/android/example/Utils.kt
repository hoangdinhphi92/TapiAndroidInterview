package com.tapi.android.example

import android.content.Context

fun calculateNoOfColumns(
    context: Context, columnWidthDp: Float
): Int {
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}
