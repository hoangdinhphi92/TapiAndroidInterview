package com.tapi.android.example.responsite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tapi.android.example.data.Photo
import com.tapi.android.example.data.PhotoUrls
import com.tapi.android.example.service.ApiClient
import com.tapi.android.example.service.UnSplashService
import com.tapi.android.example.service.models.RootObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotosUnplashRes {
   private var apiClient : UnSplashService? = null
    private var call :  Call<List<RootObject>>? = null
    val listPhotoLive : MutableLiveData<List<Photo>> = MutableLiveData()
    init {
        apiClient = ApiClient.client?.create(UnSplashService::class.java)
    }

    suspend fun getListPhotoFromPage(page: Int,perPage : Int = 10){
        withContext(Dispatchers.IO){
            call = apiClient?.getPhotosUnplashToPage(page,perPage)
            call?.enqueue(object : Callback<List<RootObject>> {
                override fun onResponse(call: Call<List<RootObject>>, response: Response<List<RootObject>>) {
                    val listPhoto = ArrayList<Photo>()
                    response.body()?.map {
                        val photoUrls = PhotoUrls(
                            it.urls?.raw.toString(),
                            it.urls?.full.toString(),
                            it.urls?.regular.toString(),
                            it.urls?.small.toString(),
                            it.urls?.thumb.toString()
                        )
                        listPhoto.add(Photo(it.id.toString(),it.description.toString(),photoUrls))
                    }
                    listPhotoLive.value = listPhoto
                }

                override fun onFailure(call: Call<List<RootObject>>, t: Throwable) {
                    Log.d("abc","onFailure ,${t.message}")
                    listPhotoLive.value = null
                }
            })
        }
    }

}