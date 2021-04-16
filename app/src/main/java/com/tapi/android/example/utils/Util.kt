package com.tapi.android.example.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class Util {
    companion object {
        val BASE_API_URL = "https://api.unsplash.com/photos/"
        val APPLICATION_ID = "cKakzKM1cx44BUYBnEIrrgN_gnGqt81UcE7GstJEils"
    }

}

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

fun View.getLayoutInflate(): LayoutInflater {
    return LayoutInflater.from(context)
}

fun ImageView.load(imageUrl: String?) {
    if (imageUrl != null) {
        val requestOptions = RequestOptions().centerInside()
        Glide.with(context).load(imageUrl).apply(requestOptions).centerCrop().into(this)
    }
}

fun ImageView.loadDetail(imageUrl: String?) {
    if (imageUrl != null) {
        val imgUri =
            imageUrl.toUri().buildUpon().scheme("https").build()
        val requestOptions = RequestOptions().centerInside()
        Glide.with(context).load(imgUri).apply(requestOptions).into(this)
    }
}

fun ImageView.loadImageBitmap(parentFragment: Fragment, urlThumbnail: String?) {
    if (urlThumbnail != null) {
        val imgUri =
            urlThumbnail.toUri().buildUpon().scheme("https").build()

        Glide.with(context).load(imgUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment.startPostponedEnterTransition()
                    return false
                }

            }).centerInside().into(this)
    }
}

fun calculateNoOfColumns(
    context: Context, columnWidthDp: Float): Int {
    val displayMetrics = context.resources.displayMetrics
    val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
    return (screenWidthDp / columnWidthDp + 0.5).toInt()
}

fun Context.isInternetAvailable(): Boolean {
    var result = false
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

fun NavController.checkTopScreenWithID(id: Int): Boolean {
    return this.currentBackStackEntry?.destination?.id == id
}

