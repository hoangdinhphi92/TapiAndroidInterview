package com.tapi.android.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tapi.android.example.R
import com.tapi.android.example.databinding.DetailImageLayoutBinding

class DetailFragment : Fragment() {
   private var _binding : DetailImageLayoutBinding? = null
    private val binding get() = _binding!!
    private val safeArg: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailImageLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            binding.detaiImg.transitionName = resources.getString(R.string.detail_transition_name)
        }
        val requestOptions: RequestOptions = RequestOptions().optionalFitCenter()
        val imgUrl = safeArg.imgUrl
        val id = safeArg
        if (!imgUrl.isNullOrEmpty()){
//            Glide.with(requireContext())
//                .load(imgUrl)
//                .apply(requestOptions)
//                .error(R.color.gray)
//                .placeholder(R.color.gray)
//                .into(binding.detaiImg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}