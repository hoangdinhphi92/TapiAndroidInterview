package com.tapi.android.example.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.LoadingViewItem
import com.tapi.android.example.databinding.ItemLoadingBinding
import com.tapi.android.example.listener.LoadingItemListener

class LoadingItemViewHolder(
    val itemBinding: ItemLoadingBinding
) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(loadingItem: LoadingViewItem) {
        if (loadingItem.isLoading) {
            itemBinding.progressBar.visibility = View.VISIBLE
            itemBinding.errorLoad.visibility = View.INVISIBLE
            itemBinding.btnTryAgain.visibility = View.INVISIBLE
        } else {
            itemBinding.progressBar.visibility = View.INVISIBLE
            itemBinding.errorLoad.visibility = View.VISIBLE
            itemBinding.btnTryAgain.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(
            viewGroup: ViewGroup,
            listener: LoadingItemListener
        ): LoadingItemViewHolder {
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            binding.listener = listener
            return LoadingItemViewHolder(binding)

        }
    }

}