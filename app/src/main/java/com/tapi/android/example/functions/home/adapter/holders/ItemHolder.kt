package com.tapi.android.example.functions.home.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.util.Utils.Companion.loadImageHolder
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.event.OnActionCallBack

class ItemHolder(
    private val binding: ItemPhotoBinding,
    val mCallBack: OnActionCallBack
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PhotoItemView.PhotoItem) {
        binding.item = item
        binding.ivItem.loadImageHolder(item.photo.photoUrls.thumb)
        initViews(item)
    }

    private fun initViews(item: PhotoItemView.PhotoItem) {
        binding.ivItem.setOnClickListener { view ->
            mCallBack.onClickItem(view = view, url = item.photo.photoUrls.regular)
        }
    }


    companion object {
        fun create(parent: ViewGroup, event: OnActionCallBack): ItemHolder {
            return ItemHolder(

                ItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), event
            )
        }
    }

}