package com.tapi.android.example.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tapi.android.example.R

@BindingAdapter(value = ["setImageUrl"])
fun ImageView.bindImageUrl(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.photo_placeholder)
        .into(this)
}
