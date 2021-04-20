package com.tapi.android.example.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.android.example.screen.home.data.PhotoItem
import com.tapi.android.example.screen.home.data.toPhotoItem
import com.tapi.android.example.service.UnSplashService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {

    private val _photoItems = MutableLiveData<List<PhotoItem>>()
    val photoItems: LiveData<List<PhotoItem>> get() = _photoItems

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> get() = _error

    private var loadMoreJob: Job? = null
    private var page = 1

    init {
        refresh()
    }

    public fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            page = 1

            try {
                val photos = UnSplashService.service.queryPhotos()
                if (photos.isNotEmpty()) {
                    _photoItems.value = photos
                        .map { it.toPhotoItem() }
                        .plus(PhotoItem.Loading)
                } else {
                    _error.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = true
            }

            _loading.value = false
        }
    }

    fun loadMore() {
        if (loadMoreJob?.isActive == true)
            return

        loadMoreJob = viewModelScope.launch {
            try {
                val photos = UnSplashService.service.queryPhotos(page + 1)
                if (photos.isNotEmpty()) {
                    val newPhotoItems = photos
                        .map { it.toPhotoItem() }

                    adMorePhotoItems(newPhotoItems)
                    page++
                } else
                    addErrorPhotoItem()
            } catch (e: Exception) {
                e.printStackTrace()
                addErrorPhotoItem()
            }
        }
    }

    private fun adMorePhotoItems(items: List<PhotoItem.Data>) {
        val photoItems =
            _photoItems.value?.filterIsInstance(PhotoItem.Data::class.java)?.toMutableList()
        photoItems ?: return

        _photoItems.value = mutableListOf<PhotoItem>().apply {
            addAll(photoItems)
            addAll(items)
            add(PhotoItem.Loading)
        }
    }

    private fun addErrorPhotoItem() {
        val photoItems =
            _photoItems.value?.filterIsInstance(PhotoItem.Data::class.java)?.toMutableList()
        photoItems ?: return

        _photoItems.value = mutableListOf<PhotoItem>().apply {
            addAll(photoItems)
            add(PhotoItem.Error)
        }
    }


}