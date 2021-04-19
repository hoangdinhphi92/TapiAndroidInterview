package com.tapi.android.example.functions.home.adapter.holders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.databinding.AgainItemBinding
import com.tapi.android.example.functions.home.screen.HomeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AgainHolder(val binding: AgainItemBinding, val model: HomeModel) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.btLoad.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                showView(true)

                delay(500)
                showView(false)

                model.queryPhotos(binding.root.context)
            }
        }
    }

    private fun showView(rs: Boolean) {
        if (rs) {
            binding.pgAgain.visibility = View.VISIBLE
            binding.errTv.visibility = View.INVISIBLE
            binding.btLoad.visibility = View.INVISIBLE
        } else {
            binding.pgAgain.visibility = View.INVISIBLE
            binding.errTv.visibility = View.VISIBLE
            binding.btLoad.visibility = View.VISIBLE
        }

    }


    companion object {
        fun create(parentView: ViewGroup, viewModel: HomeModel): AgainHolder {
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