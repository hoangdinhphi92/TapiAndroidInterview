package com.tapi.android.example.fragment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.UnSplashItem
import com.tapi.android.example.databinding.ItemLoadMoreBinding

class ItemLoadingViewHolder(private val binding: ItemLoadMoreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: UnSplashItem, listener: () -> Unit) {
        if (item is UnSplashItem.LoadingItem) {
            binding.groupError.visibility = if (item.isLoading) View.GONE else View.VISIBLE
            binding.groupLoading.visibility = if (item.isLoading) View.VISIBLE else View.GONE
            binding.tryAgain.setOnClickListener {
                listener.invoke()
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): ItemLoadingViewHolder {
            val binding =
                ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemLoadingViewHolder(binding)
        }
    }
}