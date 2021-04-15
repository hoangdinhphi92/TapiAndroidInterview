package com.tapi.android.example.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Photo(
    val id: String,
    val description: String,
    val urls: PhotoUrls
)

data class PhotoUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)