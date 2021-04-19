package com.tapi.android.example.functions.main.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.Utils.Companion.loadImage
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.event.OnActionCallBack

class MainHolder(private val binding: ItemPhotoBinding, val mCallBack: OnActionCallBack) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PhotoItemView.PhotoItem) {

        binding.ivItem.loadImage(item.photo.photoUrls.thumb)
        initViews(item)
    }

    private fun initViews(item: PhotoItemView.PhotoItem) {
        binding.ivItem.setOnClickListener { view ->
            mCallBack.onClickItem(view = view, url = item.photo.photoUrls.raw)
        }
    }


    companion object {
        fun create(parent: ViewGroup, event: OnActionCallBack): MainHolder {
            return MainHolder(
                ItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), event
            )
        }
    }

}