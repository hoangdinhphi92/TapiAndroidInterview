package com.tapi.android.example.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("goneIf")
fun View.goneIf(goneIf: Boolean) {
    visibility = if (goneIf)
        View.GONE
    else
        View.VISIBLE
}