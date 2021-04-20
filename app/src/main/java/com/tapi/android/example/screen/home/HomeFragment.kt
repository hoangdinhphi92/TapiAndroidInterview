package com.tapi.android.example.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.R
import com.tapi.android.example.data.Photo
import com.tapi.android.example.databinding.FragmentHomeBinding
import com.tapi.android.example.screen.detail.DetailFragmentArgs
import com.tapi.android.example.screen.home.adapter.PHOTO_ITEM_TYPE
import com.tapi.android.example.screen.home.adapter.PhotoGridAdapter
import com.tapi.android.example.screen.home.data.PhotoItem

class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val adapter: PhotoGridAdapter by lazy {
        PhotoGridAdapter(viewModel, this::onPhotoClicked)
    }

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

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initRecycleView()

        viewModel.photoItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRecycleView() {
        val manager = GridLayoutManager(requireContext(), 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == PHOTO_ITEM_TYPE)
                    1
                else
                    3
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = manager

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            /*override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastPosition = manager.findLastVisibleItemPosition()
                    val lastItem = adapter.currentList[lastPosition]
                    if (lastItem is PhotoItem.Loading)
                        viewModel.loadMore()
                }
            }*/

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition = manager.findLastVisibleItemPosition()
                val lastItem = adapter.currentList[lastPosition]
                if (lastItem is PhotoItem.Loading)
                    viewModel.loadMore()
            }
        })
    }

    private fun onPhotoClicked(view: View, photo: Photo) {
        val extras = FragmentNavigatorExtras(view to getString(R.string.detail_transition_name))
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(photo)
        findNavController().navigate(action, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}