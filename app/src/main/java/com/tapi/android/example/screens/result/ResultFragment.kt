package com.tapi.android.example.screens.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentResultBinding
import com.tapi.android.example.screens.result.adapter.ListPhotoItemAdapter

class ResultFragment : Fragment(){

    private var _binding: FragmentResultBinding? = null
    private val binding: FragmentResultBinding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()

    private lateinit var adapterGrid: ListPhotoItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        viewModel.getPhoto()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observerData()
    }

    private fun observerData() {
        viewModel.photo.observe(viewLifecycleOwner) {
            adapterGrid.submitList(it)
        }
        viewModel.photoLoadError.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                if (error == NO_INTERNET) {
                    Toast.makeText(requireContext(), NO_INTERNET, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRecyclerView(){
        adapterGrid = ListPhotoItemAdapter(viewModel)
        val gridLayoutManager = GridLayoutManager(
            requireContext(), viewModel.columnsCount
        )

        binding.listRv.apply {
            adapter = adapterGrid
            setHasFixedSize(true)
            layoutManager = gridLayoutManager

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}