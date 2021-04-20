package com.tapi.android.example.screens

import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.FadeThroughProvider
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
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
        override fun onClickedPhotoItem(photoViewItem: PhotoViewItem, imageView: View) {
            try {
                val extras =
                    FragmentNavigatorExtras(imageView to getString(R.string.detail_transition_name))
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        photoViewItem.photo.urls.regular
                    )
                findNavController().navigate(
                    action,
                    extras
                )
            } catch (e: Exception) {
                Log.d("abba", "error: ${e.message}")
                e.printStackTrace()
            }

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
        initToolbar()
        initSharedElement()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        bindEvent()
        initRecyclerview()
        viewModel.queryPhotos()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSharedElement() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }
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

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            title = getString(R.string.home_fragment)
        }
    }

    private fun initRecyclerview() = with(binding.rvPhoto) {
        val spanCount = 3
        val rvPadding = resources.getDimension(R.dimen.rv_padding)
        val itemSize = (resources.displayMetrics.widthPixels - rvPadding) / spanCount

        this@HomeFragment.adapter =
            PhotoItemAdapter(
                itemSize.toInt(),
                this@HomeFragment.photoItemListener,
                this@HomeFragment.loadingItemListener
            )
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
        setHasFixedSize(true)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
                if (recyclerView.childCount - lastVisibleItem < 3) {
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