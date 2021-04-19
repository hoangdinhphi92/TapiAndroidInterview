package com.tapi.android.example.functions.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tapi.android.example.databinding.FrgMainBinding
import com.tapi.android.example.functions.bases.BaseFragment
import com.tapi.android.example.util.Constance

class MainFragment : BaseFragment() {
    private var _binding: FrgMainBinding? = null
    val binding: FrgMainBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrgMainBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.mainTvDetailBack.setOnClickListener {
            sendAction(Constance.ACTION_CLICK_BACK_DETAIL_TO_HOME)
            binding.mainTvActionbarHome.visibility = View.VISIBLE
            binding.mainTvDetailBack.visibility = View.GONE
        }
    }

    override fun onActionReceived(actionName: String, data: Any?): Boolean {

        if (actionName == Constance.ACTION_CLICK_PHOTO) {
            binding.mainTvActionbarHome.visibility = View.GONE
            binding.mainTvDetailBack.visibility = View.VISIBLE
        }
        if (actionName == Constance.ACTION_CLICK_BACK_PRESS) {
            binding.mainTvActionbarHome.visibility = View.VISIBLE
            binding.mainTvDetailBack.visibility = View.GONE
        }
        return super.onActionReceived(actionName, data)
    }


}