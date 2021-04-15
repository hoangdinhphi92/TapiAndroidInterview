package com.tapi.android.example.functions.main.m001main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.data.Photo
import com.tapi.android.example.databinding.FragmentMainBinding
import com.tapi.android.example.functions.main.adapter.MainAdapter


class MainFragment : Fragment() {

    private val mainModel: MainModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!
    private lateinit var mainAdapter: MainAdapter


    private val observerList = Observer<List<Photo>> {
        if (it.isNotEmpty()) {
            mainAdapter.submitList(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainAdapter = MainAdapter(requireContext())
        mainModel.imagesData.observe(this, observerList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initViews()
        initLists()
        recycleListener()
        return binding.root
    }

    private fun initLists() {
        binding.rvPhoto.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvPhoto.adapter = mainAdapter
    }

    private fun initViews() {
        lifecycleScope.launchWhenResumed {
            mainModel.queryPhotos()
        }
    }

    private fun recycleListener() {

        binding.rvPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {

                    val gridLayoutManager: GridLayoutManager =
                        binding.rvPhoto.layoutManager as GridLayoutManager

                    val visibleItemCount = gridLayoutManager.childCount
                    val passVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = mainAdapter.itemCount
                        if ((visibleItemCount + passVisibleItem) > total) {
                            Log.d("TAG", "onScrolled: ")
                            mainModel.loadMore()
                            /* if (!CommonUtils.isNetworkConnected(activity as Activity)) {
                                showToast("internet error")
                                return
                            }

                            var i = mainModel.loadMore()*/
                        }
                }
            }
        })
    }


}