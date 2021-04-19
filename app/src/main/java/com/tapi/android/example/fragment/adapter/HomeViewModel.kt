package com.tapi.android.example.fragment.adapter

import android.util.Log
import androidx.lifecycle.*
import com.tapi.android.example.data.Photo
import com.tapi.android.example.data.PhotoUrls
import com.tapi.android.example.responsite.PhotosUnplashRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class StatusLoadingPhoto{
    LOADING,DONE,EMPTY
}
const val ID_ERROR_LOADMORE = "-1"
const val ID_HOLDER_IMAGE = "-2"
class HomeViewModel: ViewModel() {
    private var listPhotos : MediatorLiveData<List<Photo>> = MediatorLiveData()
    private var statusLoadingPhoto = MutableLiveData(StatusLoadingPhoto.DONE)
    private var page = 0
    private var perPage = 10
    private var photosUnplashRes : PhotosUnplashRes = PhotosUnplashRes()

    val obUnplashRes = Observer<List<Photo>>{
        if (it.isNullOrEmpty()){
            statusLoadingPhoto.value = StatusLoadingPhoto.EMPTY
        }else{
            if (listPhotos.value != null){
                if (!listPhotos.value.isNullOrEmpty()){
                    val listOld = ArrayList<Photo>(listPhotos.value)
                    listPhotos.value = listOld.filter { it.id != null && it.id != ID_ERROR_LOADMORE && it.id != ID_HOLDER_IMAGE}
                }
                val listOld = ArrayList<Photo>(listPhotos.value)
                listOld.addAll(it)
                listOld.add(Photo(null,"", null))
                listPhotos.value = listOld
            }else{
                val listOld = ArrayList<Photo>(it)
                listOld.add(Photo(null,"", null))
                listPhotos.value = listOld
            }
            statusLoadingPhoto.value = StatusLoadingPhoto.DONE
        }
    }
    init {
        listPhotos.addSource(photosUnplashRes.listPhotoLive,obUnplashRes)
    }

    fun getListPhotos() : LiveData<List<Photo>>{
        return listPhotos
    }

    fun nextPage(){
        viewModelScope.launch {
            if (statusLoadingPhoto.value == StatusLoadingPhoto.LOADING){
                  return@launch
            }
            statusLoadingPhoto.value = StatusLoadingPhoto.LOADING
            page += 1
            photosUnplashRes.getListPhotoFromPage(page,perPage)
        }
    }

    fun setPerPage(perPage : Int){
        this.perPage = perPage
    }

    fun addNoInternetHollder() {
        viewModelScope.launch {
            statusLoadingPhoto.value = StatusLoadingPhoto.DONE
            var sizeList = 0
            val oldList = ArrayList<Photo>()
            if (!listPhotos.value.isNullOrEmpty()){
                listPhotos.value?.let {
                    val filter = it.filter { photo ->  photo.id == ID_ERROR_LOADMORE || photo.id == ID_HOLDER_IMAGE }
                    if (filter.isNotEmpty()){
                        return@launch
                    }
                    it.forEach{ item ->
                        oldList.add(item.copy())
                    }
                    sizeList = oldList.size -1
                }
            }
            val i1 = perPage + sizeList
            if (i1 %3 == 0){
                sizeList += perPage
            }else{
                for (i in 0..100) {
                    val sizeCount = sizeList+i
                    if (sizeCount %3 ==0){
                        sizeList = sizeCount
                        break
                    }
                }
            }
            val countItemHollder = sizeList - oldList.size
            for (i in 0..countItemHollder) {
                oldList.add(Photo(ID_HOLDER_IMAGE,"",null))
            }
            oldList.add(Photo(ID_ERROR_LOADMORE,"",null))
            listPhotos.value = oldList.filter { it.id != null }
        }
    }


}