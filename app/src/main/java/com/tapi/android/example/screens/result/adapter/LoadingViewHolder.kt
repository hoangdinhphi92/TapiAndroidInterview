package com.tapi.android.example.screens.result.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.ItemLoadMoreBinding

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            val binding =
                ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadingViewHolder(binding.root)
        }
    }
}