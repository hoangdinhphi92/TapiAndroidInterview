package com.tapi.android.example.data

sealed class UnSplashItem {
    data class Photo(
        val id: String,
        val description: String,
        val urls: PhotoUrls
    ) : UnSplashItem()

    data class LoadingItem(var isLoading: Boolean = true) : UnSplashItem()
}

data class PhotoUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)