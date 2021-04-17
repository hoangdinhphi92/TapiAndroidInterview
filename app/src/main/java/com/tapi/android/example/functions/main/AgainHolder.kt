package com.tapi.android.example.functions.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.AgainItemBinding
import com.tapi.android.example.functions.main.screen.MainModel
import kotlinx.coroutines.*



class AgainHolder(val binding: AgainItemBinding, val model: MainModel) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.btLoad.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                binding.pgAgain.visibility = View.VISIBLE
                binding.errTv.visibility =View.GONE
                delay(500)
                binding.pgAgain.visibility = View.GONE
                binding.errTv.visibility =View.VISIBLE
                Log.d("manhnq", "bind: click again")
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