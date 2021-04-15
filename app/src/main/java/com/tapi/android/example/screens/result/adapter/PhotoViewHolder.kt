package com.tapi.android.example.screens.result.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.screens.result.ResultViewModel
import com.tapi.android.example.utils.getLayoutInflate
import com.tapi.android.example.utils.load

class PhotoViewHolder(
    private val viewBinding: ItemPhotoBinding,
    private val viewModel: ResultViewModel,
    private val context: Context
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun build(photoItem: PhotoViewItem) {
        setOnClick(photoItem)
        viewBinding.photoImg.load(photoItem.photo.urls.thumb)
    }

    private fun setOnClick(photo: PhotoViewItem){
        viewBinding.photoImg.setOnClickListener {
            viewModel.onClickItem(photo)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            viewModel: ResultViewModel,
            context: Context
        ): PhotoViewHolder {
            return PhotoViewHolder(
                ItemPhotoBinding.inflate(
                    parent.getLayoutInflate(), parent, false
                ), viewModel, context
            )
        }
    }

}