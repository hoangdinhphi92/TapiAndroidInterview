package com.tapi.android.example.screens.detail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.tapi.android.example.R
import com.tapi.android.example.databinding.FragmentDetailBinding
import com.tapi.android.example.utils.load
import com.tapi.android.example.utils.themeColor

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private val urlsThumbs: String by lazy { args.urlThumbs }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set up MaterialContainerTransform transition as sharedElementEnterTransition.
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 300
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Set up postponed enter transition.
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.backImg.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.pictureImg.load(urlsThumbs)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}