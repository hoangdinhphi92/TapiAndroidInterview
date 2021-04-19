package com.tapi.android.example

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadUrl(url:String){
    Glide.with(context).load(url).placeholder(R.color.white1).into(this)
}