package com.tapi.android.example.data

interface ViewItem {
}

data class PhotoViewItem(val photo: Photo) : ViewItem
data class LoadingViewItem(val isLoading: Boolean) : ViewItem

enum class LoadingState {
    LOADING,
    LOADED,
    ERROR
}