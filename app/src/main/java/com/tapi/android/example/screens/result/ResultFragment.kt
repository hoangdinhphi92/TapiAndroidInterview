package com.tapi.android.example.screens.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialElevationScale
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentResultBinding
import com.tapi.android.example.screens.result.adapter.LOAD_MORE_TYPE
import com.tapi.android.example.screens.result.adapter.ListPhotoItemAdapter
import com.tapi.android.example.screens.result.adapter.OnClickItemListener
import com.tapi.android.example.screens.result.adapter.PHOTO_TYPE

class ResultFragment : Fragment(), OnClickItemListener, View.OnClickListener{

    private var _binding: FragmentResultBinding? = null
    private val binding: FragmentResultBinding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()

    private lateinit var adapterGrid: ListPhotoItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            if (!error.isNullOrEmpty()) {
                setViewError(error)
            }
        }
    }

    private fun setViewIdle(){
        binding.errorTxt.visibility = View.GONE
        binding.getDataBtn.visibility = View.GONE
        binding.getDataBtn.text = "TRY AGAIN"
        binding.listRv.visibility = View.VISIBLE
    }

    private fun setViewError(error: String){
        binding.errorTxt.visibility = View.VISIBLE
        binding.getDataBtn.visibility = View.VISIBLE
        binding.errorTxt.text = error
    }

    private fun initRecyclerView(){
        adapterGrid = ListPhotoItemAdapter(this)
        val gridLayoutManager = GridLayoutManager(
            requireContext(), viewModel.columnsCount
        )

        binding.listRv.itemAnimator = null

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
                    else -> -1
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelected(view: View, urlThumb: String) {

        exitTransition = MaterialElevationScale(false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }

        val emailCardDetailTransitionName = getString(R.string.detail_transition_name)
        val extras = FragmentNavigatorExtras(view to emailCardDetailTransitionName)
        val directions = ResultFragmentDirections.actionResultFragmentToDetailFragment(urlThumb)
        findNavController().navigate(directions, extras)
    }

    override fun onClick(v: View?) {
        if(v == binding.getDataBtn) {
            viewModel.getPhoto()
        }
    }

}