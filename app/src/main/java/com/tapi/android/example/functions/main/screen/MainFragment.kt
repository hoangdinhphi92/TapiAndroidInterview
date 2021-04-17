package com.tapi.android.example.functions.main.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
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
import com.tapi.android.example.R
import com.tapi.android.example.Utils
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.databinding.FragmentMainBinding
import com.tapi.android.example.event.OnActionCallBack
import com.tapi.android.example.functions.main.adapter.MainAdapter
import com.tapi.android.example.functions.main.adapter.TypeItem


class MainFragment : Fragment(), OnActionCallBack {

    private val mainModel: MainModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!
    private lateinit var mainAdapter: MainAdapter


    private val observerList = Observer<List<PhotoItemView>> {
        if (it.isNotEmpty()) {
            setViewIdle()
            mainAdapter.submitList(it)
        }
    }

    private val observerErr = Observer<TypeNetwork> {
        if (it == TypeNetwork.NO_INTERNET && mainModel.imagesData.value.isNullOrEmpty()) {
            setViewError("error")
        }
    }

    private fun setViewIdle() {
        binding.errTv.visibility = View.INVISIBLE
        binding.getDataBtn.visibility = View.INVISIBLE
        binding.getDataBtn.text = getString(R.string.getdata)
        binding.rvPhoto.visibility = View.VISIBLE
    }

    private fun setViewError(error: String) {
        binding.errTv.visibility = View.VISIBLE
        binding.getDataBtn.visibility = View.VISIBLE
        binding.getDataBtn.text = getString(R.string.try_again)
        binding.errTv.text = error
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiverNetwork()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainAdapter = MainAdapter(requireContext(), mainModel, this)
        observerData()
        initLists()

    }

    private fun observerData() {
        mainModel.imagesData.observe(viewLifecycleOwner, observerList)
        mainModel.errData.observe(viewLifecycleOwner, observerErr)
    }


    private fun initLists() {

        val gridLayoutManager = GridLayoutManager(
            requireContext(), mainModel.columnsCount
        )

        binding.rvPhoto.layoutManager = gridLayoutManager
        binding.rvPhoto.adapter = mainAdapter
        recycleListener()

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mainAdapter.getItemViewType(position)) {
                    TypeItem.LOADDING_ITEM.ordinal -> mainModel.columnsCount
                    TypeItem.PHOTO_ITEM.ordinal -> 1
                    TypeItem.AGAIN_ITEM.ordinal -> mainModel.columnsCount
                    else -> -1
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

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            lifecycleScope.launchWhenResumed {
                if (Utils.isNetworkConnected(requireContext())) {
                    setViewIdle()
                    mainModel.queryPhotos(requireContext())
                } else {
                  /*  setViewError("error")*/
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
        /* val action = MainFragmentDirections.actionMainFragmentToDetailFragment(url)
         findNavController().navigate(action)*/
    }


}