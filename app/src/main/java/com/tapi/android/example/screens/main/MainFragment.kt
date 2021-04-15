package com.tapi.android.example.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentMainBinding
import com.tapi.android.example.utils.checkTopScreenWithID

class MainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClick()
    }

    private fun initOnClick() {
        binding.getDataBtn.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.getDataBtn -> {
                if (!findNavController().checkTopScreenWithID(R.id.resultFragment)) {
                    findNavController().navigate(R.id.action_mainFragment2_to_resultFragment)
                }
            }
        }
    }


}