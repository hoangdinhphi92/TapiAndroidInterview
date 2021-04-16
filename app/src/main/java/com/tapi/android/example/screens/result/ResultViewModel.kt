package com.tapi.android.example.screens.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.android.example.data.PhotoViewItem
import com.tapi.android.example.service.RetrofitClient
import com.tapi.android.example.utils.calculateNoOfColumns
import com.tapi.android.example.utils.isInternetAvailable
import kotlinx.coroutines.*

const val NO_INTERNET = "No Internet"

class ResultViewModel(val app: Application) : AndroidViewModel(app) {

    private val retrofitClient = RetrofitClient().getPhotosService()

    private var job: Job? = null

    private val _photos = MutableLiveData<List<PhotoViewItem>>()
    val photos: LiveData<List<PhotoViewItem>> get() = _photos

    private val _photosLoadError = MutableLiveData<String?>()
    val photoLoadError: LiveData<String?> get() = _photosLoadError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    var columnsCount = 1
    private var _page = 1

    init {
        columnsCount = calculateNoOfColumns(app, 120f)
    }

    fun getPhoto() {
        job?.cancel()
        if (!app.isInternetAvailable()) {
            _photosLoadError.value = NO_INTERNET
            onError(NO_INTERNET)
        }
        else {
            val result = _photos.value?.filter {
                it is PhotoViewItem.PhotoItem
            }

            val totalList = mutableListOf<PhotoViewItem>()
            if (result != null) {
                totalList.addAll(result)
            }

            _loading.value = true
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = retrofitClient.queryPhotos(page = _page)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        val listPhotoViewItem = ArrayList<PhotoViewItem>()
                        list?.forEach{
                            listPhotoViewItem.add(PhotoViewItem.PhotoItem(it))
                        }
                        totalList.addAll(listPhotoViewItem)
                        totalList.add(PhotoViewItem.Loading)

                        _photos.value = totalList
                        _page += 1
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        _photosLoadError.value = message
        _loading.value = false
        removeLoadMoreItem()
    }

    private fun removeLoadMoreItem() {
        val list = _photos.value
        _photos.value =
            list?.filterIsInstance<PhotoViewItem.PhotoItem>()
    }

    fun isLoadMoreItem(position: Int): Boolean {
        return if (job?.isCompleted == true) (_photos.value?.get(position) is PhotoViewItem.Loading)
        else false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}