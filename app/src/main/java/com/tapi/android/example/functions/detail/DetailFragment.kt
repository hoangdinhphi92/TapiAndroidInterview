package com.tapi.android.example.functions.detail

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.tapi.android.example.R
import com.tapi.android.example.Utils.Companion.loadImage
import com.tapi.android.example.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    val binding: FragmentDetailBinding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment2
            duration = 300
            scrimColor = Color.TRANSPARENT
//            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(args.url).thumbnail(0.05f).transition(
            DrawableTransitionOptions.withCrossFade()).into(binding.ivImg)
        binding.ivImg.loadImage(args.url)

    }


}