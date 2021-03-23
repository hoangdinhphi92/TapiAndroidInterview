package com.tapi.android.example.service

import com.tapi.android.example.data.Photo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface UnSplashService {

    // Edit This
    @GET("/photos")
    suspend fun queryPhotos(): List<Photo>

}