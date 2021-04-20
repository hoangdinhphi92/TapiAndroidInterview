package com.tapi.android.example.fragment.home

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
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.tapi.android.example.R
import com.tapi.android.example.calculateNoOfColumns
import com.tapi.android.example.databinding.HomeFragmentBinding
import com.tapi.android.example.fragment.home.adapter.ITEM_LOADING
import com.tapi.android.example.fragment.home.adapter.ITEM_PHOTO
import com.tapi.android.example.fragment.home.adapter.UnSplashAdapter
import com.tapi.android.example.fragment.home.viewmodel.HomeViewModel
import com.tapi.android.example.fragment.home.viewmodel.State

class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding: HomeFragmentBinding get() = _binding!!

    private var adapterSplash: UnSplashAdapter? = null

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initView()
        observerData()
    }

    private fun initView() {
        binding.tryAgain.setOnClickListener {
            viewModel.loadPhotoUnSplash()
        }

        adapterSplash = UnSplashAdapter(
            UnSplashAdapter.OnItemListener(
                this::onClickItemPhotoListener,
                this::onClickReload
            )
        )
        val calculateNoOfColumns = calculateNoOfColumns(requireContext(), 120f)
        val gridLayoutManager =
            GridLayoutManager(context, calculateNoOfColumns)
        binding.recyclerView.apply {
            adapter = adapterSplash
            layoutManager = gridLayoutManager
            setHasFixedSize(true)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (position != -1 && viewModel.isLoadMoreItem(position)) {
                        viewModel.loadPhotoUnSplash()
                    }
                }
            })
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapterSplash?.getItemViewType(position)) {
                        ITEM_PHOTO -> 1
                        ITEM_LOADING -> calculateNoOfColumns
                        else -> -1
                    }
                }
            }
        }
    }

    private fun observerData() {
        viewModel.listData.observe(viewLifecycleOwner) {
            adapterSplash?.submitList(it)
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                State.SUCCESS, State.DEFAULT -> {
                    binding.groupError.visibility = View.GONE
                    binding.groupLoading.visibility = View.GONE
                }
                State.FAIL -> {
                    binding.groupError.visibility = View.VISIBLE
                    binding.groupLoading.visibility = View.GONE
                }
                State.LOADING -> {
                    binding.groupError.visibility = View.GONE
                    binding.groupLoading.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickItemPhotoListener(imageView: View, url: String) {
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)
        val bundle = Bundle()
        bundle.putString("key_url", url)
        val findNavController = findNavController()
        if (findNavController.previousBackStackEntry == null) {
            findNavController.navigate(
                R.id.action_homeFragment_to_previewFragment,
                bundle,
                null,
                extras
            )
        }
    }

    private fun onClickReload() {
        viewModel.loadPhotoUnSplash()
    }
}