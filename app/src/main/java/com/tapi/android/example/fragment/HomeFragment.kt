package com.tapi.android.example.fragment

import android.os.Bundle
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.tapi.android.example.R
import com.tapi.android.example.Utils
import com.tapi.android.example.data.Photo
import com.tapi.android.example.databinding.HomeFragmentLayoutBinding
import com.tapi.android.example.fragment.adapter.*
import kotlin.math.roundToInt


class HomeFragment : Fragment(), AdapterPhotos.OnClickImage {
    private var adapter: AdapterPhotos? = null
    private var _binding: HomeFragmentLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.main_graph)

    private val obListPhoto = Observer<List<Photo>> {
        binding.progressBar.visibility = View.INVISIBLE
        adapter?.submitList(it)
        binding.recyclerViewPhoto.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)

        reenterTransition = TransitionInflater.from(context).inflateTransition(
            android.R.transition.move
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        binding.progressBar.visibility = View.VISIBLE
        checkViewInternetCollection()
        binding.tryAgainBtn.setOnClickListener {
            checkViewInternetCollection()
        }

    }

    private fun initView() {
        binding.progressBar.visibility = View.INVISIBLE
        hideErrorInternet()
        viewModel.getListPhotos().observe(viewLifecycleOwner, obListPhoto)
        caculatorPerPage()
        setupRecyclerView()
        viewModel.nextPage()
    }

    private fun hideErrorInternet() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorloadTxt.visibility = View.INVISIBLE
        binding.tryAgainBtn.visibility = View.INVISIBLE
    }

    private fun showErrorInternet() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.errorloadTxt.visibility = View.VISIBLE
        binding.tryAgainBtn.visibility = View.VISIBLE
    }

    private fun caculatorPerPage() {
        var perPage = 10
        val widthScreen = Utils.getWidthScreen()
        val heightScreen = Utils.getHeightScreen()
        if (heightScreen > widthScreen) {
            val itemWidth = widthScreen / 3f
            val numberRow = heightScreen / itemWidth
            perPage = (numberRow * 3).roundToInt()
        }
        viewModel.setPerPage(perPage)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewPhoto.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
            override fun getSpanSize(i: Int): Int {
                //loader
                val id = adapter?.currentList?.get(i)?.id
                return if (id == null || id == "-1") {
                    3
                } else 1
            }
        })
        binding.recyclerViewPhoto.layoutManager = gridLayoutManager
        adapter = AdapterPhotos(requireContext(), this)
        binding.recyclerViewPhoto.adapter = adapter
        binding.recyclerViewPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val connectionType = Utils.getConnectionType(requireContext())
                    if (connectionType > 0) {
                        adapter?.let {
                            val size = it.currentList.size
                            if (it.currentList[size - 1].id != ID_ERROR_LOADMORE) {
                                viewModel.nextPage()
                            }
                        }
                    } else {
                        viewModel.addNoInternetHollder()
                    }
                }
            }
        })

        val dividerItemDecoration = GridItemDecorator(
            requireContext(),
            resources.getDimension(R.dimen.photos_list_spacing).toInt(),
            3
        )
        binding.recyclerViewPhoto.addItemDecoration(dividerItemDecoration)

    }

    private fun nextPage() {
        val connectionType = Utils.getConnectionType(requireContext())
        if (connectionType > 0) {
            viewModel.nextPage()
        } else {
            viewModel.addNoInternetHollder()
        }
    }

    private fun checkViewInternetCollection() {
        val connectionType = Utils.getConnectionType(requireContext())
        if (connectionType > 0) {
            initView()
        } else {
            showErrorInternet()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clickImage(photo: Photo, view: View, imageView: ImageView, currentPosition: Int) {
        if (photo.id != null && photo.id != ID_ERROR_LOADMORE && photo.id != ID_HOLDER_IMAGE) {
            val extras = FragmentNavigatorExtras(
                view to photo.id
            )
            val actionHomeFragmentToDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                photo.id, photo.urls?.regular
            )
            findNavController().navigate(actionHomeFragmentToDetailFragment, extras)
        }
    }

    override fun clickTryAgainBtn() {
        nextPage()
    }
}