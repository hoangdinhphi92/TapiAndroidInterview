package com.tapi.android.example.listener

import android.view.View
import com.tapi.android.example.data.PhotoViewItem

interface PhotoItemListener {
    fun onClickedPhotoItem(photoViewItem: PhotoViewItem, imageView: View)
}