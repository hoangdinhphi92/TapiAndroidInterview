package com.tapi.android.example.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.HomeLoadingItemBinding

class LoadingViewHolder(binding: HomeLoadingItemBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            return LoadingViewHolder(
                HomeLoadingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}