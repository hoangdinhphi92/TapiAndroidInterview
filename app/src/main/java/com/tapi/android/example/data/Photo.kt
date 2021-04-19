package com.tapi.android.example.data

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