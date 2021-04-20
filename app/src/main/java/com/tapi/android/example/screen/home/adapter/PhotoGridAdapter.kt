package com.tapi.android.example.screen.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.Photo
import com.tapi.android.example.screen.home.HomeViewModel
import com.tapi.android.example.screen.home.data.PhotoItem
import java.lang.IllegalArgumentException

const val PHOTO_ITEM_TYPE = 1;
const val LOADING_ITEM_TYPE = 2;
const val ERROR_ITEM_TYPE = 3;

class PhotoGridAdapter(private val viewModel: HomeViewModel, private val listener: (View, Photo) -> Unit) : ListAdapter<PhotoItem, RecyclerView.ViewHolder>(PhotoGridDiffUnit()) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is PhotoItem.Loading -> LOADING_ITEM_TYPE
            is PhotoItem.Error -> ERROR_ITEM_TYPE
            is PhotoItem.Data -> PHOTO_ITEM_TYPE
            else -> throw IllegalArgumentException("Not support data type!!!!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            PHOTO_ITEM_TYPE -> PhotoViewHolder.create(parent, listener)
            LOADING_ITEM_TYPE -> LoadingViewHolder.create(parent)
            ERROR_ITEM_TYPE -> ErrorViewHolder.create(parent, viewModel)
            else -> throw IllegalArgumentException("Not support view type!!!!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoViewHolder) {
            val photo = (getItem(position) as PhotoItem.Data).photo
            holder.bind(photo)
        }
    }
}


class PhotoGridDiffUnit : DiffUtil.ItemCallback<PhotoItem>() {
    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        if (oldItem is PhotoItem.Data && newItem is PhotoItem.Data) {
            return oldItem.photo.id == newItem.photo.id
        }

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return true
    }

}
