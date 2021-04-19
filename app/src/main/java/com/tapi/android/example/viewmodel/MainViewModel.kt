package com.tapi.android.example.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.tapi.android.example.data.*
import com.tapi.android.example.service.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun ArrayList<ViewItem>.removeLoadingItem() {
    val loadingItemIndex = find {
        it is LoadingViewItem
    }
    remove(loadingItemIndex)
}

class MainViewModel : ViewModel() {

    private var page = 1
    private var per_page = 15
    private val retrofit = RetrofitBuilder.unSplashService

    private val _listPhotos = MediatorLiveData<List<ViewItem>>()
    val listPhotos: LiveData<List<ViewItem>> get() = _listPhotos

    private var queryPhotosJob: Job? = null

    private val loadMoreState = MutableLiveData<LoadingState>()

    private var _loadingState = MediatorLiveData<LoadingState>()
    val loadingState get() = _loadingState

    init {
        _loadingState.addSource(_listPhotos) {
            checkErrorLoad()
        }

        _loadingState.addSource(this.loadMoreState) {
            checkErrorLoad()
        }


        _listPhotos.addSource(this.loadMoreState) {
            when (it) {
                LoadingState.LOADING -> {
                    val listPhotos = _listPhotos.value
                    if (listPhotos != null) {
                        //add loading item
                        val newList = ArrayList(listPhotos)
                        newList.removeLoadingItem()
                        newList.add(LoadingViewItem(true))
                        _listPhotos.value = newList
                    }
                }

                LoadingState.LOADED -> {
                    val listPhotos = _listPhotos.value
                    if (listPhotos != null) {
                        val newList = ArrayList(listPhotos)
                        newList.removeLoadingItem()
                        _listPhotos.value = newList
                    }
                }

                LoadingState.ERROR -> {
                    val listPhotos = _listPhotos.value
                    if (listPhotos != null) {
                        val newList = ArrayList(listPhotos)
                        newList.removeLoadingItem()
                        newList.add(LoadingViewItem(false))
                        _listPhotos.value = newList
                    }
                }
            }
        }

    }

    private fun checkErrorLoad() {
        val listPhotos = _listPhotos.value
        val isLoading = this.loadMoreState.value

        if (listPhotos == null && isLoading == LoadingState.ERROR) {
            _loadingState.value = LoadingState.ERROR
        } else if (listPhotos == null && isLoading == LoadingState.LOADING) {
            _loadingState.value = LoadingState.LOADING
        } else {
            _loadingState.value = LoadingState.LOADED
        }

    }

    fun queryPhotos() {
        if (queryPhotosJob?.isActive == true) {
            return
        }
        queryPhotosJob = viewModelScope.launch {
            this@MainViewModel.loadMoreState.value = LoadingState.LOADING

            try {
                val response = withContext(Dispatchers.IO) {
                    retrofit.queryPhotos(page, per_page)
                }

                if (response.isSuccessful) {
                    val newListPhotos = response.body()
                    if (newListPhotos != null && newListPhotos.isNotEmpty()) {
                        addNewPhotos(newListPhotos)
                        page++
                    }
                    this@MainViewModel.loadMoreState.value = LoadingState.LOADED
                } else {
                    this@MainViewModel.loadMoreState.value = LoadingState.ERROR
                }
            } catch (e: Exception) {
                this@MainViewModel.loadMoreState.value = LoadingState.ERROR
            }
        }
    }

    private fun addNewPhotos(newList: List<Photo>) {
        val oldListPhotos = _listPhotos.value ?: emptyList()
        val newListPhotos = ArrayList(oldListPhotos)
        newListPhotos.addAll(newList.map {
            PhotoViewItem(it)
        })
        _listPhotos.value = newListPhotos
    }


}