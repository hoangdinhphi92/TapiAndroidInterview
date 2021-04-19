package com.tapi.android.example.fragment.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.fragment.home.adapter.viewholder.ItemLoadingViewHolder
import com.tapi.android.example.fragment.home.adapter.viewholder.ItemPhotoViewHolder
import com.tapi.android.example.data.UnSplashItem


val ITEM_PHOTO = 0
val ITEM_LOADING = 1

class UnSplashAdapter(private val listener: OnItemListener) :
    ListAdapter<UnSplashItem, RecyclerView.ViewHolder>(DiffUtils()) {


    data class OnItemListener(
        val onClickItemListener: (View, String) -> Unit,
        val onClickLoadMore: () -> Unit
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_PHOTO) {
            ItemPhotoViewHolder.create(parent)
        } else {
            ItemLoadingViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is UnSplashItem.Photo) {
            ITEM_PHOTO
        } else {
            ITEM_LOADING
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_PHOTO) {
            val holderItem = holder as ItemPhotoViewHolder
            holderItem.bindView(getItem(position),listener.onClickItemListener)
        } else {
            val loadingHolder = holder as ItemLoadingViewHolder
            loadingHolder.bindView(getItem(position), listener.onClickLoadMore)
        }
    }

}

private class DiffUtils : DiffUtil.ItemCallback<UnSplashItem>() {
    override fun areItemsTheSame(oldItem: UnSplashItem, newItem: UnSplashItem): Boolean {
        if (oldItem is UnSplashItem.Photo && newItem is UnSplashItem.Photo) {
            return oldItem.id == newItem.id
        } else if (oldItem is UnSplashItem.LoadingItem && newItem is UnSplashItem.LoadingItem) {
            return true
        }
        return false
    }

    override fun areContentsTheSame(oldItem: UnSplashItem, newItem: UnSplashItem): Boolean {
        if (oldItem is UnSplashItem.Photo && newItem is UnSplashItem.Photo) {
            return oldItem.id == newItem.id
        } else if (oldItem is UnSplashItem.LoadingItem && newItem is UnSplashItem.LoadingItem) {
            return oldItem.isLoading == newItem.isLoading
        }
        return false
    }

    override fun getChangePayload(oldItem: UnSplashItem, newItem: UnSplashItem): Any? {
        return true
    }

}