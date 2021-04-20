package com.tapi.android.example.screens.home.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.R
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.utils.getLayoutInflate
import com.tapi.android.example.utils.load
import java.util.*

class PhotoViewHolder(
    private val viewBinding: ItemPhotoBinding,
    private val listener: OnClickItemListener
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private var photoViewItem : PhotoViewItem.PhotoItem? = null

    fun build(data: PhotoViewItem.PhotoItem){
        initOnclick()

        updatePicture(data)
    }

    fun updatePicture(data: PhotoViewItem.PhotoItem) = with(viewBinding) {
        id = data.photo.id
        photoViewItem = data

        photoImg.load(data.photo.urls.thumb)
    }

    private fun initOnclick(){
        viewBinding.photoImg.setOnClickListener {
            val item = photoViewItem
            if (item != null) {
                Log.e("MTHAI", "initOnclick: ${String.format(Locale.ENGLISH, "photo_item_\$1%s", item.photo.id)} --- ${item.photo.urls.regular}" )
                listener.onSelected(it, item.photo.urls.regular)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: OnClickItemListener
        ): PhotoViewHolder {
            return PhotoViewHolder(
                ItemPhotoBinding.inflate(
                    parent.getLayoutInflate(), parent, false
                ), listener
            )
        }
    }
}

interface OnClickItemListener{
    fun onSelected(view: View, urlThumbRegular : String)
}