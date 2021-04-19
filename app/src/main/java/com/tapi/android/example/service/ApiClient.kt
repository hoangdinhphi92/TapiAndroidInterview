package com.tapi.android.example.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL_API = "https://api.unsplash.com"

    var retrofit: Retrofit? = null

    init {
        val httpClient = OkHttpClient.Builder().hostnameVerifier { s, st -> true }.build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}