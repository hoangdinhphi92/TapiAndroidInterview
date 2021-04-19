package com.tapi.android.example.functions.detail

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentDetailBinding
import com.tapi.android.example.event.OnCallBackToFragment
import com.tapi.android.example.functions.bases.BaseFragment
import com.tapi.android.example.themeColor
import com.tapi.android.example.util.Constance
import com.tapi.android.example.util.Utils.Companion.loadImageHolder


class DetailFragment : BaseFragment(), OnCallBackToFragment {

    private var _binding: FragmentDetailBinding? = null
    val binding: FragmentDetailBinding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = buildContainerTransform()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun buildContainerTransform() =
        MaterialContainerTransform()
            .apply {
                drawingViewId = R.id.my_nav
                scrimColor = Color.TRANSPARENT
                duration = 200
                setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        getMainActivity()?.setOnCallBack(this)

        return binding.root
    }

    override fun onActionReceived(actionName: String, data: Any?): Boolean {
        if (actionName == Constance.ACTION_CLICK_BACK_DETAIL_TO_HOME) {
            requireActivity().onBackPressed()
        }
        return super.onActionReceived(actionName, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivImg.loadImageHolder(args.url)


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun backPressToFrg() {
        lifecycleScope.launchWhenResumed {
            sendAction(Constance.ACTION_CLICK_BACK_PRESS)
            requireActivity().onBackPressed()
        }
    }


}