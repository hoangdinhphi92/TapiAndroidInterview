package com.tapi.android.example.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.tapi.android.example.BR
import com.tapi.android.example.R
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.databinding.ItemPhotoBinding
import com.tapi.android.example.listener.PhotoItemListener
import com.tapi.android.example.viewmodel.MainViewModel

class PhotoItemViewHolder(val itemBinding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(photoViewItem: PhotoViewItem) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            itemBinding.ivPhoto.transitionName = String.format(
                itemView.context.getString(R.string.photo_item_transition_name),
                photoViewItem.photo.id
            )
        }
        itemBinding.setVariable(BR.photoItem, photoViewItem)
        itemBinding.executePendingBindings()
    }

    companion object {
        fun newInstance(
            viewGroup: ViewGroup,
            itemSize: Int,
            listener: PhotoItemListener
        ): PhotoItemViewHolder {
            val binding =
                ItemPhotoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

            binding.root.updateLayoutParams {
                width = itemSize
                height = itemSize
            }

            val padding = viewGroup.context.resources.getDimension(R.dimen.rv_padding).toInt()
            binding.root.setPadding(
                padding,
                padding,
                padding,
                padding
            )
            binding.listener = listener
            return PhotoItemViewHolder(binding)
        }
    }

}