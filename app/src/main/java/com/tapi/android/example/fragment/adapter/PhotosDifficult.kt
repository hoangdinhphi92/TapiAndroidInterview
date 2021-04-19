package com.tapi.android.example.fragment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tapi.android.example.data.Photo

class PhotosDifficult: DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.urls == oldItem.urls
    }

}
