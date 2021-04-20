package com.tapi.android.example.functions.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentDetailBinding
import com.tapi.android.example.event.OnCallBackToFragment
import com.tapi.android.example.functions.bases.BaseFragment
import com.tapi.android.example.themeColor
import com.tapi.android.example.util.Constance


class DetailFragment : BaseFragment(), OnCallBackToFragment {

    private var _binding: FragmentDetailBinding? = null
    val binding: FragmentDetailBinding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = buildContainerTransform()
            sharedElementReturnTransition = buildContainerTransform()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun buildContainerTransform() =
        MaterialContainerTransform()
            .apply {
                drawingViewId = R.id.my_nav
                scrimColor = Color.TRANSPARENT
                duration = 300
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
        postponeEnterTransition()
        view.doOnPreDraw {
            Glide.with(requireContext()).load(args.url).centerCrop()
                .error(R.drawable.photo_placeholder).listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                })
                .into(binding.ivImg as ImageView)
        }


    }

    override fun backPressToFrg() {
        lifecycleScope.launchWhenResumed {
            sendAction(Constance.ACTION_CLICK_BACK_PRESS)
            requireActivity().onBackPressed()
        }
    }


}