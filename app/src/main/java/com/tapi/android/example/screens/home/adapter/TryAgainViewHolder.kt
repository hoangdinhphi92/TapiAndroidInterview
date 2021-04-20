package com.tapi.android.example.screens.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.ItemTryAgainBinding
import com.tapi.android.example.screens.home.ResultViewModel
import com.tapi.android.example.utils.getLayoutInflate

class TryAgainViewHolder(private val viewBinding: ItemTryAgainBinding, private val  viewModel: ResultViewModel) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun build(){
        viewBinding.tryAgainBtn.setOnClickListener {
            viewModel.getPhoto()
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: ResultViewModel): TryAgainViewHolder {
            return TryAgainViewHolder(
                ItemTryAgainBinding.inflate(
                    parent.getLayoutInflate(), parent, false
                ), viewModel
            )
        }
    }
}