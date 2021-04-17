package com.tapi.android.example.data


sealed class PhotoItemView {
    data class PhotoItem(val photo: Photo) : PhotoItemView()
    object LoadingItem : PhotoItemView()
    object AgainItem : PhotoItemView()
}
