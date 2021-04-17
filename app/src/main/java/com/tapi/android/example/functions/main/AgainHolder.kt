package com.tapi.android.example.functions.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.AgainItemBinding
import com.tapi.android.example.functions.main.screen.MainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgainHolder(val binding: AgainItemBinding, val model: MainModel) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.btLoad.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                model.queryPhotos(binding.root.context)
            }
        }
    }


    companion object {
        fun create(parentView: ViewGroup, viewModel: MainModel): AgainHolder {
            return AgainHolder(
                AgainItemBinding.inflate(
                    LayoutInflater.from(parentView.context),
                    parentView,
                    false
                ), viewModel
            )
        }
    }
}