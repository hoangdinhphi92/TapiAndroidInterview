package com.tapi.android.example.functions.home.screen

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.service.APIService
import com.tapi.android.example.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun List<PhotoItemView>.convertList(): List<PhotoItemView.PhotoItem> {
    val lists = ArrayList<PhotoItemView.PhotoItem>()
    this.forEach {
        if (it is PhotoItemView.PhotoItem) {
            lists.add(it)
        }
    }
    return lists
}

class HomeModel(application: Application) : AndroidViewModel(application) {

    private var _images: MutableLiveData<List<PhotoItemView>> = MutableLiveData()
    val imagesData: LiveData<List<PhotoItemView>> get() = _images


    private val _errData = MutableLiveData<TypeNetwork>()
    val errData: LiveData<TypeNetwork> get() = _errData

    private var currentPage = 1


    suspend fun queryPhotos(context: Context): Boolean {
        var rs: Boolean
        if (!Utils.isNetworkConnected(context)) {
            _errData.value = TypeNetwork.NO_INTERNET
            checkValidNetwork(TypeNetwork.NO_INTERNET)
            rs = false
        } else {
            try {
                checkValidRemove()
                val rsList = _images.value?.convertList()

                val images = mutableListOf<PhotoItemView>()
                if (rsList != null && !rsList.isEmpty()) {
                    images.addAll(rsList)
                }
                Log.d("TAG", "queryPhotos: call api")
                withContext(Dispatchers.IO) {
                    val response = APIService.retrofit.queryPhotos(page = currentPage)

                    Log.d("TAG", "queryPhotos: ${response.code()}")

                    withContext(Dispatchers.Main) {
                        when {
                            response.code() == 200 -> {
                                val listTmps = ArrayList<PhotoItemView>()
                                response.body()?.map {
                                    listTmps.add(PhotoItemView.PhotoItem(it))
                                }

                                images.addAll(listTmps)
                                images.add(PhotoItemView.LoadingItem)

                                _images.value = images
                                currentPage++
                                rs = true
                            }
                            response.code() == 504 -> {

                                _errData.value = TypeNetwork.CALL_TIME_OUT
                                rs = false
                            }
                            else -> {
                                _errData.value = TypeNetwork.SERVER_NOT_FOUND
                                rs = false
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                rs = false
            }
        }
        return rs
    }

    private fun checkValidLoadMore() {
        val list = _images.value
        list?.let {
            if (it.indexOf(PhotoItemView.LoadingItem) != -1) {
                _images.value = it.convertList()
            }
        }

    }

    private fun checkValidRemove() {
        val list = _images.value
        list?.let {
            if (it.indexOf(PhotoItemView.AgainItem) != -1) {
                _images.value = it.convertList()
            }
        }
    }

    private fun checkValidNetwork(message: TypeNetwork) {
        _errData.value = message
        checkValidLoadMore()

        val list = _images.value
        if (!list.isNullOrEmpty()) {

            if (!list.contains(PhotoItemView.AgainItem)) {
                val listNew = mutableListOf<PhotoItemView>()
                listNew.addAll(list)
                listNew.add(PhotoItemView.AgainItem)
                _images.value = listNew
            }

        }
    }

}

enum class TypeNetwork {
    NO_INTERNET, CALL_TIME_OUT, SERVER_NOT_FOUND
}