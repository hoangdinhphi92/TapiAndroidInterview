package com.tapi.android.example.screen.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapi.android.example.R
import com.tapi.android.example.data.Photo
import com.tapi.android.example.databinding.HomeLoadingItemBinding
import com.tapi.android.example.databinding.HomePhotoItemBinding

class PhotoViewHolder(private val binding: HomePhotoItemBinding, val listener: (View, Photo) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo) {
        Glide.with(binding.photo)
            .load(photo.urls.thumb)
            .placeholder(R.drawable.photo_placeholder)
            .into(binding.photo)

        ViewCompat.setTransitionName(binding.photo, "photo_item_${photo.id}")
        binding.photo.setOnClickListener {
            listener(it, photo)
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: (View, Photo) -> Unit): PhotoViewHolder {
            return PhotoViewHolder(
                HomePhotoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

}