package com.tapi.android.example.screens.result.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.screens.result.ResultViewModel

const val PHOTO_TYPE = 0
const val LOAD_MORE_TYPE = 1
const val TRY_AGAIN_TYPE = 2

const val UPDATE_PICTURE = 113

class ListPhotoItemAdapter(private val viewModel : ResultViewModel, private val listener: OnClickItemListener): ListAdapter<PhotoViewItem, RecyclerView.ViewHolder>(PhotoDiffUtil())  {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            PhotoViewItem.Loading -> {
                LOAD_MORE_TYPE
            }
            PhotoViewItem.TryAgain -> {
                TRY_AGAIN_TYPE
            }
            else -> {
                PHOTO_TYPE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PHOTO_TYPE -> {
                PhotoViewHolder.create(parent, listener)
            }
            LOAD_MORE_TYPE -> {
                LoadingViewHolder.create(parent)
            }
            else -> {
                TryAgainViewHolder.create(parent, viewModel)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == PHOTO_TYPE) {
            (holder as PhotoViewHolder).build(getItem(position) as PhotoViewItem.PhotoItem)
        } else if(getItemViewType(position) == TRY_AGAIN_TYPE) {
            (holder as TryAgainViewHolder).build()
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val state = payloads.firstOrNull() as? Int
            if (state == UPDATE_PICTURE) {
                (holder as PhotoViewHolder).updatePicture(getItem(position) as PhotoViewItem.PhotoItem)
            }
        }
    }

}

class PhotoDiffUtil: DiffUtil.ItemCallback<PhotoViewItem>() {
    override fun areItemsTheSame(oldItem: PhotoViewItem, newItem: PhotoViewItem): Boolean {
        if (oldItem is PhotoViewItem.PhotoItem && newItem is PhotoViewItem.PhotoItem) {
            return oldItem.photo.id == newItem.photo.id
        }
        return false
    }

    override fun areContentsTheSame(oldItem: PhotoViewItem, newItem: PhotoViewItem): Boolean {
        return false
    }

    override fun getChangePayload(oldItem: PhotoViewItem, newItem: PhotoViewItem): Any? {
        if (oldItem is PhotoViewItem.PhotoItem && newItem is PhotoViewItem.PhotoItem) {
                return UPDATE_PICTURE
        }
        return null
    }
}