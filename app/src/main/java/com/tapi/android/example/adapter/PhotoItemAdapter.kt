package com.tapi.android.example.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.adapter.viewholder.LoadingItemViewHolder
import com.tapi.android.example.adapter.viewholder.PhotoItemViewHolder
import com.tapi.android.example.data.LoadingViewItem
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.data.ViewItem
import com.tapi.android.example.listener.LoadingItemListener
import com.tapi.android.example.listener.PhotoItemListener
import com.tapi.android.example.viewmodel.MainViewModel

const val UPDATE_LOADING_ITEM = 1

class PhotoItemAdapter(
    val itemSize: Int,
    val photoItemListener: PhotoItemListener,
    val loadingItemListener: LoadingItemListener
) :
    ListAdapter<ViewItem, RecyclerView.ViewHolder>(ViewItemDiffUtilCallBack()) {

    private val TYPE_PHOTO = 0
    private val TYPE_LOADING = 1

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is PhotoViewItem) {
            return TYPE_PHOTO
        } else {
            return TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_PHOTO) {
            return PhotoItemViewHolder.newInstance(parent, itemSize,photoItemListener)
        } else {
            return LoadingItemViewHolder.newInstance(parent,loadingItemListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is PhotoItemViewHolder && item is PhotoViewItem) {
            holder.bindData(item)
        } else if (holder is LoadingItemViewHolder && item is LoadingViewItem) {
            holder.bindData(item)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val update = payloads.firstOrNull()
        if (update == UPDATE_LOADING_ITEM) {
            val newItem = getItem(position)
            if (holder is LoadingItemViewHolder && newItem is LoadingViewItem) {
                holder.bindData(newItem)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}

class ViewItemDiffUtilCallBack : DiffUtil.ItemCallback<ViewItem>() {
    override fun areItemsTheSame(oldItem: ViewItem, newItem: ViewItem): Boolean {
        return if (oldItem is LoadingViewItem && newItem is LoadingViewItem) {
            true
        } else oldItem is PhotoViewItem && newItem is PhotoViewItem && oldItem.photo == newItem.photo
    }

    override fun areContentsTheSame(oldItem: ViewItem, newItem: ViewItem): Boolean {
        return if (oldItem is LoadingViewItem && newItem is LoadingViewItem) {
            oldItem.isLoading == newItem.isLoading
        } else if (oldItem is PhotoViewItem && newItem is PhotoViewItem) {
            oldItem.photo == newItem.photo
        } else {
            false
        }
    }

    override fun getChangePayload(oldItem: ViewItem, newItem: ViewItem): Any? {
        if (oldItem is LoadingViewItem && newItem is LoadingViewItem) {
            if (oldItem.isLoading != newItem.isLoading) {
                return UPDATE_LOADING_ITEM
            }
        }
        return super.getChangePayload(oldItem, newItem)
    }

}