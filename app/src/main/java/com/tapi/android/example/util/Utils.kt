package com.tapi.android.example.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tapi.android.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Utils {


    companion object {
        suspend fun isNetworkConnected(context: Context): Boolean = withContext(Dispatchers.IO) {
            try {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                //should check null because in airplane mode it will be null
                netInfo != null && netInfo.isConnected
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }


        fun View.loadImageHolder(url: String) {

            Glide.with(this.context).load(url).placeholder(R.drawable.photo_placeholder)
                .error(R.drawable.photo_placeholder).centerCrop().dontAnimate()
                .into(this as ImageView)
        }

    }


}