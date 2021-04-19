package com.tapi.android.example.fragment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.UnSplashItem
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.loadUrl

class ItemPhotoViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: UnSplashItem, listener: (View, String) -> Unit) {
        if (item is UnSplashItem.Photo) {
            val regular = item.urls.regular
            ViewCompat.setTransitionName(binding.image, regular)
            binding.image.loadUrl(item.urls.thumb)
            binding.image.setOnClickListener {
                listener.invoke(it, regular)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): ItemPhotoViewHolder {
            val binding =
                ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemPhotoViewHolder(binding)
        }
    }
}