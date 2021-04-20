package com.tapi.android.example.functions.home.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.platform.MaterialElevationScale
import com.tapi.android.example.R
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.databinding.FragmentHomeBinding
import com.tapi.android.example.event.OnActionCallBack
import com.tapi.android.example.event.OnCallBackToFragment
import com.tapi.android.example.functions.bases.BaseFragment
import com.tapi.android.example.functions.home.adapter.HomeAdapter
import com.tapi.android.example.functions.home.adapter.TypeItem
import com.tapi.android.example.util.Constance
import com.tapi.android.example.util.Utils
import kotlinx.coroutines.delay


class HomeFragment : BaseFragment(), OnActionCallBack, OnCallBackToFragment {

    private val mainModel: HomeModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding get() = _binding!!
    private lateinit var mainAdapter: HomeAdapter
    private var curTmp: Int = -1


    private val observerList = Observer<List<PhotoItemView>> {
        binding.pgMain.visibility = View.VISIBLE
        if (it.isNotEmpty()) {
            curTmp = it.size
            setViewIsNotEmptyList()
            mainAdapter.submitList(it)
            binding.pgMain.visibility = View.INVISIBLE
        }

    }

    private val observerErr = Observer<TypeNetwork> {
        Log.d("TAG", "observerErr: $it")
        if (mainModel.imagesData.value.isNullOrEmpty()) {

            when (it) {
                TypeNetwork.NO_INTERNET -> {
                    setViewErrorNetwork("no internet")
                }
                TypeNetwork.CALL_TIME_OUT -> {
                    setViewErrorNetwork("call time out")
                }
                TypeNetwork.SERVER_NOT_FOUND -> {
                    setViewErrorNetwork("Server Not found")
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiverNetwork()
        setToolbar()

    }

    private fun setToolbar() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        getMainActivity()?.setOnCallBack(this)
        mainAdapter = HomeAdapter(requireContext(), mainModel, this)
        observerData()
        initLists()

    }

    private fun observerData() {
        mainModel.imagesData.observe(viewLifecycleOwner, observerList)
        mainModel.errData.observe(viewLifecycleOwner, observerErr)
    }


    private fun initLists() {

        val gridLayoutManager = GridLayoutManager(
            requireContext(), 3
        )
        binding.rvPhoto.layoutManager = gridLayoutManager
        binding.rvPhoto.setHasFixedSize(true)
        binding.rvPhoto.adapter = mainAdapter
        recycleListener()

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mainAdapter.getItemViewType(position)) {
                    TypeItem.LOADDING_ITEM.ordinal -> 3
                    TypeItem.PHOTO_ITEM.ordinal -> 1
                    TypeItem.AGAIN_ITEM.ordinal -> 3
                    else -> -1
                }
            }
        }
    }

    private fun initViews() {
        binding.loadTv.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                binding.errTv.visibility = View.GONE
                binding.pgMain.visibility = View.VISIBLE
                delay(500)
                val result = mainModel.queryPhotos(requireContext())
                if (result) {
                    setViewIsNotEmptyList()
                } else {
                    setViewErrorNetwork("error")
                }
            }
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
                        lifecycleScope.launchWhenResumed {
                            Log.d("TAG", "onScrolled: ")
                            mainModel.queryPhotos(requireContext())
                        }

                    }
                }
            }
        })
    }

    private fun setViewIsNotEmptyList() {

        binding.pgMain.visibility = View.INVISIBLE
        binding.errTv.visibility = View.INVISIBLE
        binding.loadTv.apply {
            visibility = View.INVISIBLE
            text = getString(R.string.getdata)
        }
        binding.rvPhoto.visibility = View.VISIBLE
    }


    private fun setViewErrorNetwork(error: String) {
        binding.pgMain.visibility = View.INVISIBLE
        binding.errTv.apply {
            visibility = View.VISIBLE
            text = error
        }
        binding.loadTv.apply {
            text = getString(R.string.load_again)
            visibility = View.VISIBLE
        }
    }


    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            lifecycleScope.launchWhenResumed {
                if (Utils.isNetworkConnected(requireContext())) {
                    if (curTmp == -1) {
                        setViewIsNotEmptyList()
                        binding.pgMain.visibility = View.VISIBLE
                        val rs = mainModel.queryPhotos(requireContext())
                        if (rs) {
                            binding.pgMain.visibility = View.GONE
                        }
                    }
                } else {
                    if (curTmp == -1) {
                        setViewErrorNetwork("network unavailable")
                    }
                }
            }
        }
    }


    private fun registerReceiverNetwork() {

        requireActivity().registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        )
    }

    private fun unRegisterReceiverNetWork() {
        try {
            requireActivity().unregisterReceiver(networkChangeReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceiverNetWork()
    }

    override fun onClickItem(view: View, url: String) {
        sendAction(Constance.ACTION_CLICK_PHOTO)
        val action = HomeFragmentDirections.actionMainFragmentToDetailFragment(url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            exitTransition = MaterialElevationScale(false).apply {

                duration = 300
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300
            }


            val tsn = getString(R.string.detail_transition_name)
            val extras = FragmentNavigatorExtras(view to tsn)
            findNavController().navigate(action, extras)
        } else {
            findNavController().navigate(action)
        }
    }

    override fun backPressToFrg() {
        requireActivity().onBackPressed()
    }


}