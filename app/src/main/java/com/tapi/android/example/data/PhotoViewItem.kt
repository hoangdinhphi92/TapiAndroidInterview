package com.tapi.android.example.data

sealed class PhotoViewItem{
    data class PhotoItem(val photo: Photo): PhotoViewItem()
    object Loading: PhotoViewItem()
}
