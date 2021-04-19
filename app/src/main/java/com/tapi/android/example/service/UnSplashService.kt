package com.tapi.android.example.service

import com.tapi.android.example.data.UnSplashItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import retrofit2.http.Query


interface UnSplashService {

    // Edit This
    @GET("/photos/?client_id=EbOOHuUeFzALb__WYeQdQ_diRVEWzNIyNHQmm1cGu3o")
    suspend fun queryPhotos(@Query("page") page: Int, @Query("per_page") per_page: Int): List<UnSplashItem.Photo>

    companion object {
        fun getApiServer(): UnSplashService? {
            val retrofit = ApiClient.retrofit
            return retrofit?.create(UnSplashService::class.java)
        }
    }

}