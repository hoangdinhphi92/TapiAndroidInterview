package com.tapi.android.example.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tapi.android.example.R
import com.tapi.android.example.data.Photo
import com.tapi.android.example.databinding.LoadingMoreErrorBinding
import com.tapi.android.example.databinding.LoadingMoreLayoutBinding
import com.tapi.android.example.databinding.PhotoItemLayoutBinding


class AdapterPhotos(val context: Context, val itemClickListener: OnClickImage) :
    ListAdapter<Photo, RecyclerView.ViewHolder>(PhotosDifficult()) {
    var _binding: PhotoItemLayoutBinding? = null
    val binding get() = _binding!!
    var _bindingLoading: LoadingMoreLayoutBinding? = null
    val bindingLoading get() = _bindingLoading!!
    var _bindingErrorLoading: LoadingMoreErrorBinding? = null
    val bindingErrorLoading get() = _bindingErrorLoading!!
    val requestOptions: RequestOptions = RequestOptions().optionalCenterCrop()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == 0) {
            _binding = PhotoItemLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
            return PhotoViewHolder(binding)
        } else if (viewType == -1) {
            _bindingErrorLoading = LoadingMoreErrorBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
            return HolderErrorLoadMoreViewHolder(bindingErrorLoading)
        } else {
            _bindingLoading = LoadingMoreLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
            return LoadingViewHolder(bindingLoading)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val id = getItem(position).id
        return if (id != null) {
            if (id == ID_ERROR_LOADMORE) {
                -1
            } else {
                0
            }
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PhotoViewHolder)
            holder.bindData(getItem(position))
        if (holder is LoadingViewHolder)
            holder.bindData()
        if (holder is HolderErrorLoadMoreViewHolder)
            holder.bindData()
    }

    inner class LoadingViewHolder(val bind: LoadingMoreLayoutBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bindData() {
            bind.progressBar2.visibility = View.VISIBLE
        }
    }

    inner class HolderErrorLoadMoreViewHolder(val bind: LoadingMoreErrorBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bindData() {
            bind.tryAgainBtn.setOnClickListener {
                itemClickListener.clickTryAgainBtn()
            }
        }
    }

    inner class PhotoViewHolder(val bind: PhotoItemLayoutBinding) :
        RecyclerView.ViewHolder(bind.root) {
        fun bindData(photo: Photo) {
            if (photo.urls == null) {
                bind.itemPhotoImg.setImageResource(R.color.gray)
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bind.itemPhotoImg.transitionName = context.resources.getString(R.string.detail_transition_name)
                }
                Glide.with(context)
                    .load(photo.urls.thumb)
                    .apply(requestOptions)
                    .error(R.color.gray)
                    .placeholder(R.color.gray)
                    .into(bind.itemPhotoImg)
            }

            binding.itemPhotoImg.setOnClickListener {
                itemClickListener.clickImage(getItem(adapterPosition),bind.itemPhotoImg)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _binding = null
        _bindingErrorLoading = null
        _bindingLoading = null
    }

    interface OnClickImage {
        fun clickImage(photo: Photo,image : ImageView)
        fun clickTryAgainBtn()
    }
}