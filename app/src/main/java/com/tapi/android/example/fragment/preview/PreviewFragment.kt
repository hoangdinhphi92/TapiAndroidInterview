package com.tapi.android.example.fragment.preview

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import com.tapi.android.example.R
import com.tapi.android.example.databinding.PreviewFragmentBinding


class PreviewFragment : Fragment() {

    private var _binding: PreviewFragmentBinding? = null
    private val binding: PreviewFragmentBinding get() = _binding!!

    private val url by lazy {
        arguments?.getString("key_url") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(activity as AppCompatActivity) {
            supportActionBar?.title = "Detail Fragment"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            val inflateTransition = MaterialContainerTransform().apply {
                drawingViewId = R.id.nav_host_fragment
                duration = 300
                scrimColor = Color.TRANSPARENT
                setAllContainerColors(Color.WHITE)
            }
            sharedElementEnterTransition =
                inflateTransition
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PreviewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        ViewCompat.setTransitionName(binding.image, url)

        val requestOptions = RequestOptions.placeholderOf(R.color.white1)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontTransform()
            .dontAnimate()
        Glide.with(this)
            .load(url)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
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
            .into(binding.image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}