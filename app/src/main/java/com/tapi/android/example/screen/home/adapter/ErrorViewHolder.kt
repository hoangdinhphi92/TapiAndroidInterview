package com.tapi.android.example.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.HomeErrorItemBinding
import com.tapi.android.example.databinding.HomeLoadingItemBinding
import com.tapi.android.example.screen.home.HomeViewModel

class ErrorViewHolder(private val binding: HomeErrorItemBinding, private val viewModel: HomeViewModel): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.tryAgain.setOnClickListener {
            viewModel.loadMore()
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: HomeViewModel): ErrorViewHolder {
            return ErrorViewHolder(
                HomeErrorItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                viewModel
            )
        }
    }
}