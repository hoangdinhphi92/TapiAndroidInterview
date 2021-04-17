package com.tapi.android.example.functions.main.screen

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.android.example.Utils
import com.tapi.android.example.data.PhotoItemView
import com.tapi.android.example.service.APIService
import kotlinx.coroutines.*


fun List<PhotoItemView>.convertList(): List<PhotoItemView.PhotoItem> {
    val lists = ArrayList<PhotoItemView.PhotoItem>()
    this.forEach {
        if (it is PhotoItemView.PhotoItem) {
            lists.add(it)
        }
    }
    return lists
}

class MainModel(application: Application) : AndroidViewModel(application) {

    private var _images: MutableLiveData<List<PhotoItemView>> = MutableLiveData()
    val imagesData: LiveData<List<PhotoItemView>> get() = _images

    private val _errData = MutableLiveData<TypeNetwork>()
    val errData: LiveData<TypeNetwork> get() = _errData

    private var currentPage = 1
    var columnsCount = 1

    init {
        columnsCount = Utils.calculatorValue(getApplication(), 120f)
    }

    suspend fun queryPhotos(context: Context) = CoroutineScope(Dispatchers.IO).launch {

        if (!Utils.isNetworkConnected(context)) {
            withContext(Dispatchers.Main) {
                _errData.value = TypeNetwork.NO_INTERNET
                checkValidNetwork(TypeNetwork.NO_INTERNET)
            }
        } else {
            try {
                val rsList = _images.value?.convertList()

                val totalList = mutableListOf<PhotoItemView>()
                if (rsList != null && rsList.isNotEmpty()) {
                    totalList.addAll(rsList)
                }
                Log.d("TAG", "queryPhotos: call api")
                val response = APIService.retrofit.queryPhotos(page = currentPage)

                Log.d("TAG", "queryPhotos: ${response.code()}")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val list = response.body()
                        val listTmps = ArrayList<PhotoItemView>()
                        list?.forEach {
                            listTmps.add(PhotoItemView.PhotoItem(it))
                        }
                        checkValidRemove()

                        totalList.addAll(listTmps)
                        totalList.add(PhotoItemView.LoadingItem)

                        _images.value = totalList
                        currentPage++
                    } else {
                        _errData.value = TypeNetwork.SERVER_NOT_FOUND
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
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

enum class TypeNetwork() {
    NO_INTERNET, CALL_TIME_OUT, SERVER_NOT_FOUND
}