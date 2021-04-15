package com.tapi.android.example.functions.main.m001main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.android.example.data.Photo
import com.tapi.android.example.service.APIService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainModel : ViewModel() {

    private var _imagesData: MutableLiveData<List<Photo>> = MutableLiveData()
    val imagesData: LiveData<List<Photo>> get() = _imagesData
    private var currentPage = 1

    private var _loading = MutableLiveData<Int>()
    val loading: LiveData<Int>
        get() = _loading

    suspend fun queryPhotos(): LiveData<List<Photo>> {
        val data = APIService.retrofit.queryPhotos(page = currentPage)
        _loading.value = 0

        data.body()?.let {
            _imagesData.value = it
            _loading.value = 1
        }

        return imagesData
    }


    fun loadMore(): Int {
        currentPage++

        val handler = CoroutineExceptionHandler { _, exception ->
            _loading.value = -1
            Log.d("TAG", "CoroutineExceptionHandler got $exception")
        }
        viewModelScope.launch(handler) {
            callGetPhotoToLoadMore(currentPage)
        }

        return currentPage
    }

    private suspend fun callGetPhotoToLoadMore(currentPage: Int): LiveData<List<Photo>> {

        _loading.value = 0
        val moreList = APIService.retrofit.queryPhotos(page = currentPage)
//        checkValide(response.code())
        val currList = _imagesData.value?.toMutableList()

        currList?.let {
            moreList.body()?.let {
                currList.addAll(it)
            }
            _imagesData.value = currList
        }

        _loading.value = 1
        return imagesData


    }
}