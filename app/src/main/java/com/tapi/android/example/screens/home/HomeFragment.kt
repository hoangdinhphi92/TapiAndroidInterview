package com.tapi.android.example.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialElevationScale
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentHomeBinding
import com.tapi.android.example.screens.home.adapter.*
import com.tapi.android.example.utils.checkTopScreenWithID

const val HOME_FRAGMENT_NAME = "Home Fragment"

class HomeFragment : Fragment(), OnClickItemListener, View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()

    private lateinit var adapterGrid: ListPhotoItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        initRecyclerView()
        binding.getDataBtn.setOnClickListener(this)
        observerData()
    }

    private fun observerData() {
        viewModel.photos.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                setViewIdle()
                adapterGrid.submitList(it)
            }
        }
        viewModel.photoLoadError.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty() && viewModel.photos.value.isNullOrEmpty()) {
                setViewError(error)
            }
        }
    }

    private fun setViewIdle() {
        binding.errorTxt.visibility = View.INVISIBLE
        binding.getDataBtn.visibility = View.INVISIBLE
        binding.getDataBtn.text = getString(R.string.get_data)
        binding.listRv.visibility = View.VISIBLE
    }

    private fun setViewError(error: String) {
        binding.errorTxt.visibility = View.VISIBLE
        binding.errorTxt.text = error
        binding.getDataBtn.visibility = View.VISIBLE
        binding.getDataBtn.text = getString(R.string.try_again)
    }


    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            supportActionBar?.title = HOME_FRAGMENT_NAME
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    private fun initRecyclerView() {
        adapterGrid = ListPhotoItemAdapter(viewModel, this)
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
                    val position = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (position != -1 && viewModel.isLoadMoreItem(position)) {
                        viewModel.getPhoto()
                    }
                }
            })
        }

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapterGrid.getItemViewType(position)) {
                    LOAD_MORE_TYPE -> viewModel.columnsCount
                    PHOTO_TYPE -> 1
                    TRY_AGAIN_TYPE -> viewModel.columnsCount
                    else -> -1
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelected(view: View, urlThumbRegular: String) {

        exitTransition = MaterialElevationScale(false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }

        val detailTransitionName = getString(R.string.detail_transition_name)
        val extras = FragmentNavigatorExtras(view to detailTransitionName)
        val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(urlThumbRegular)

        if (!findNavController().checkTopScreenWithID(R.id.detailFragment)) {
            findNavController().navigate(directions, extras)
        }
    }

    override fun onClick(v: View?) {
        if (v == binding.getDataBtn) {
            viewModel.getPhoto()
        }
    }

}