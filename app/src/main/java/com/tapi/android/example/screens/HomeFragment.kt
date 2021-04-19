package com.tapi.android.example.screens

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.R
import com.tapi.android.example.adapter.PhotoItemAdapter
import com.tapi.android.example.data.LoadingState
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.databinding.FragmentHomeBinding
import com.tapi.android.example.listener.LoadingItemListener
import com.tapi.android.example.listener.PhotoItemListener
import com.tapi.android.example.viewmodel.MainViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private val viewModel by viewModels<MainViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val photoItemListener = object : PhotoItemListener {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onClickedPhotoItem(photoViewItem: PhotoViewItem, imageView: View) {
            val extras =
                FragmentNavigatorExtras(imageView to imageView.transitionName)
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    photoViewItem.photo.urls.regular,
                    imageView.transitionName
                )
            findNavController().navigate(
                action,
                extras
            )
        }

    }

    private val loadingItemListener = object : LoadingItemListener {
        override fun onClickedTryAgain() {
            viewModel.queryPhotos()
        }

    }

    private lateinit var adapter: PhotoItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvent()
        initRecyclerview()
        viewModel.queryPhotos()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindEvent() {
        binding.btnTryAgain.setOnClickListener(this)
    }

    private fun observeData() {
        viewModel.listPhotos.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadingState.observe(this.viewLifecycleOwner) {
            when (it) {
                LoadingState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnTryAgain.visibility = View.INVISIBLE
                    binding.errorLoad.visibility = View.INVISIBLE
                }

                LoadingState.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.btnTryAgain.visibility = View.VISIBLE
                    binding.errorLoad.visibility = View.VISIBLE
                }

                LoadingState.LOADED -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.btnTryAgain.visibility = View.INVISIBLE
                    binding.errorLoad.visibility = View.INVISIBLE
                }

            }
        }

    }

    private fun initRecyclerview() = with(binding.rvPhoto) {
        val spanCount = 3
        val rvPadding = resources.getDimension(R.dimen.rv_padding)
        val itemSize = (resources.displayMetrics.widthPixels - rvPadding) / spanCount

        this@HomeFragment.adapter =
            PhotoItemAdapter(itemSize.toInt(), this@HomeFragment.photoItemListener, this@HomeFragment.loadingItemListener)
        adapter = this@HomeFragment.adapter

        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (this@HomeFragment.adapter.currentList[position] is PhotoViewItem) {
                    1
                } else {
                    spanCount
                }
            }
        }

        layoutManager = gridLayoutManager

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
                if (recyclerView.childCount - lastVisibleItem < 4) {
                    viewModel.queryPhotos()
                }
            }
        })

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnTryAgain -> {
                viewModel.queryPhotos()
            }
        }
    }

}