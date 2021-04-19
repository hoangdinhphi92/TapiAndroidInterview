package com.tapi.android.example.service

import com.tapi.android.example.data.Photo
import com.tapi.android.example.service.models.RootObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface UnSplashService {

    // Edit This
    @GET("/photos")
    suspend fun queryPhotos(): List<Photo>

    @GET("/photos")
    fun getPhotosUnplash(): Call<List<RootObject>>?

    @GET("/photos")
    fun getPhotosUnplashToPage(@Query("page") page: Int = 1,@Query("per_page") perPage: Int = 10): Call<List<RootObject>>?

}