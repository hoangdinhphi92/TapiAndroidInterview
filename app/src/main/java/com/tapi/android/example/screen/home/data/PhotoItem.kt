package com.tapi.android.example.screen.home.data

import com.tapi.android.example.data.Photo

sealed class PhotoItem {
    data class Data(val photo: Photo): PhotoItem()
    object Loading: PhotoItem()
    object Error: PhotoItem()
}

fun Photo.toPhotoItem() = PhotoItem.Data(this)