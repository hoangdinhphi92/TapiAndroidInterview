package com.tapi.android.example.functions.home.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.event.OnActionCallBack
import com.tapi.android.example.functions.home.adapter.holders.AgainHolder
import com.tapi.android.example.functions.home.adapter.holders.ItemHolder
import com.tapi.android.example.functions.home.adapter.holders.LoadingHolder
import com.tapi.android.example.functions.home.screen.HomeModel


class HomeAdapter(val mContext: Context, val model: HomeModel, val mCallback: OnActionCallBack) :
    ListAdapter<PhotoItemView, RecyclerView.ViewHolder>(PhotoDiff()) {


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            PhotoItemView.LoadingItem -> {
                TypeItem.LOADDING_ITEM.ordinal
            }
            PhotoItemView.AgainItem -> {
                TypeItem.AGAIN_ITEM.ordinal
            }
            else -> {
                TypeItem.PHOTO_ITEM.ordinal
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TypeItem.LOADDING_ITEM.ordinal -> {
                LoadingHolder.create(parent)
            }
            TypeItem.AGAIN_ITEM.ordinal -> {
                AgainHolder.create(parent, model)
            }
            else -> {
                ItemHolder.create(parent, mCallback)
            }
        }

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val holer = holder as ItemHolder

            holer.bind(getItem(position) as PhotoItemView.PhotoItem)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TypeItem.PHOTO_ITEM.ordinal) {
            (holder as ItemHolder).bind(getItem(position) as PhotoItemView.PhotoItem)
        } else if (getItemViewType(position) == TypeItem.AGAIN_ITEM.ordinal) {
            (holder as AgainHolder).bind()
        }
    }


}

class PhotoDiff : DiffUtil.ItemCallback<PhotoItemView>() {
    override fun areItemsTheSame(oldItem: PhotoItemView, newItem: PhotoItemView): Boolean {
        if (oldItem is PhotoItemView.PhotoItem && newItem is PhotoItemView.PhotoItem) {
            return oldItem.photo.id == newItem.photo.id
        }
        return false
    }
    override fun areContentsTheSame(oldItem: PhotoItemView, newItem: PhotoItemView): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: PhotoItemView, newItem: PhotoItemView): Any? {
        if (oldItem is PhotoItemView.PhotoItem && newItem is PhotoItemView.PhotoItem) {
            return newItem.photo.id
        }
        return null
    }

}

enum class TypeItem(num: Int) {
    PHOTO_ITEM(0), LOADDING_ITEM(1), AGAIN_ITEM(2)
}