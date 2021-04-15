package com.tapi.android.example.data

import com.google.gson.annotations.SerializedName

data class Photo(

    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("urls") val photoUrls: PhotoUrls
)

data class PhotoUrls(
    @SerializedName("raw")
    val raw: String,

    @SerializedName("full")
    val full: String,

    @SerializedName("regular")
    val regular: String,

    @SerializedName("small")
    val small: String,

    @SerializedName("thumb")
    val thumb: String
) {
    override fun toString(): String {
        return "Picture(raw='$raw', full='$full', regular='$regular', small='$small', thumb='$thumb')"
    }
}
