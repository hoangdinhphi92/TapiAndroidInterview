package com.tapi.android.example.screens.result.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.screens.result.ResultViewModel

const val PHOTO_TYPE = 111
const val LOAD_MORE_TYPE = 112

class ListPhotoItemAdapter(private val viewModel: ResultViewModel): ListAdapter<PhotoViewItem, RecyclerView.ViewHolder>(PhotoDiffUtil())  {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).loading == true) {
            LOAD_MORE_TYPE
        } else {
            PHOTO_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PHOTO_TYPE) {
            PhotoViewHolder.create(parent, viewModel, parent.context)
        } else {
            LoadingViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == PHOTO_TYPE) {
            (holder as PhotoViewHolder).build(getItem(position))
        }
    }

}

class PhotoDiffUtil: DiffUtil.ItemCallback<PhotoViewItem>() {
    override fun areItemsTheSame(oldItem: PhotoViewItem, newItem: PhotoViewItem): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PhotoViewItem, newItem: PhotoViewItem): Boolean {
        return true
    }
}