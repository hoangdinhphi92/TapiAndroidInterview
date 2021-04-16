package com.tapi.android.example.screens.result.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.utils.getLayoutInflate
import com.tapi.android.example.utils.load

class PhotoViewHolder(
    private val viewBinding: ItemPhotoBinding,
    private val listener: OnClickItemListener,
    private val context: Context
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun build(data: PhotoViewItem.PhotoItem) = with(viewBinding){
        init(data)

        photoItem = data
        photoImg.load(data.photo.urls.thumb)
    }

    private fun init(data: PhotoViewItem.PhotoItem){
        viewBinding.photoImg.setOnClickListener {
            listener.onSelected(it, data.photo.urls.thumb)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: OnClickItemListener, context: Context
        ): PhotoViewHolder {
            return PhotoViewHolder(
                ItemPhotoBinding.inflate(
                    parent.getLayoutInflate(), parent, false
                ), listener, context
            )
        }
    }
}


interface OnClickItemListener{
    fun onSelected(view: View, urlThumb : String)
}