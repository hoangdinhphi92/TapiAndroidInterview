package com.tapi.android.example

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
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

        fun View.loadImage(url: String) {
            Glide.with(this.context).load(url).into(this as ImageView)
        }

        fun calculatorValue(
            context: Context, columnWidthDp: Float): Int {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
    }


}