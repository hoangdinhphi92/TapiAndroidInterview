package com.tapi.android.example.screens.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tapi.android.example.data.Photo
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
    val photo: LiveData<List<PhotoViewItem>> get() = _photos

    private val _photosLoadError = MutableLiveData<String?>()
    val photoLoadError: LiveData<String?> get() = _photosLoadError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    var columnsCount = 1

    init {
        columnsCount = calculateNoOfColumns(app, 120f)
    }

    fun getPhoto() {
        job?.cancel()
        if (!app.isInternetAvailable()) _photosLoadError.value = NO_INTERNET
        else {
            _loading.value = true
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = retrofitClient.queryPhotos()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        val listPhotoViewItem = ArrayList<PhotoViewItem>()
                        list?.forEach{
                            listPhotoViewItem.add(PhotoViewItem(it))
                        }
                        _photos.value = listPhotoViewItem
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
    }

    fun onClickItem(photo: PhotoViewItem) {

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}