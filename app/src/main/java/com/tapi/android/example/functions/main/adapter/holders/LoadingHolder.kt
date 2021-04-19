package com.tapi.android.example.functions.main.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.LoadingItemBinding

class LoadingHolder(private val itemView: LoadingItemBinding) : RecyclerView.ViewHolder(itemView.root) {


    companion object {
        fun create(parentView: ViewGroup): LoadingHolder {
            return LoadingHolder(
                LoadingItemBinding.inflate(
                    LayoutInflater.from(parentView.context),
                    parentView,
                    false
                )
            )

        }
    }
}