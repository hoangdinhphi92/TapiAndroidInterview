package com.tapi.android.example.fragment.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tapi.android.example.data.UnSplashItem
import com.tapi.android.example.service.UnSplashService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


enum class State {
    SUCCESS, FAIL, LOADING, DEFAULT
}

class HomeViewModel : ViewModel() {
    private var _listData = MutableLiveData<List<UnSplashItem>>()
    val listData get() = _listData

    private val _state = MutableLiveData<State>(State.DEFAULT)
    val state get() = _state

    private var page = 1

    private var job: Job? = null

    init {
        loadPhotoUnSplash()
    }

    fun loadPhotoUnSplash(perPage: Int = 12) {
        if (job == null || job?.isCompleted == true) {
            job = viewModelScope.launch {
                if (_listData.value.isNullOrEmpty())
                    _state.value = State.LOADING
                else {
                    val list =
                        listData.value?.filter { it is UnSplashItem.Photo }?.toMutableList()
                    list?.add(UnSplashItem.LoadingItem(true))
                    _listData.value = list?.toList()
                }
                val queryPhotos = getListPhoto(perPage)
                setDataToList(queryPhotos)
            }
        }
    }

    private suspend fun getListPhoto(perPage: Int): List<UnSplashItem.Photo>? =
        withContext(Dispatchers.IO) {
            try {
                return@withContext UnSplashService.getApiServer()?.queryPhotos(page, perPage)
            } catch (ex: Exception) {
                return@withContext null
            }
        }

    private fun setDataToList(queryPhotos: List<UnSplashItem.Photo>?) {
        if (!queryPhotos.isNullOrEmpty()) {
            if (_listData.value.isNullOrEmpty()) {
                _state.value = State.SUCCESS
            }
            var listFind =
                _listData.value?.filter { it !is UnSplashItem.LoadingItem }?.toMutableList()
            if (listFind == null) listFind = mutableListOf()
            listFind.addAll(queryPhotos)
            listFind.add(UnSplashItem.LoadingItem(true))
            _listData.value = listFind.toList()
            page++
        } else {
            if (_listData.value.isNullOrEmpty()) {
                _state.value = State.FAIL
            } else {
                var listFind =
                    _listData.value?.filter { it !is UnSplashItem.LoadingItem }?.toMutableList()
                if (listFind == null) listFind = mutableListOf()
                listFind.add(UnSplashItem.LoadingItem(false))
                _listData.value = listFind.toList()
            }
        }
    }

    fun isLoadMoreItem(index: Int): Boolean {
        if (job?.isCompleted == true) {
            return _listData.value?.get(index) is UnSplashItem.LoadingItem
        }
        return false
    }
}