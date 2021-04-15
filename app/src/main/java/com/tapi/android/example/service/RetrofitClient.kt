package com.tapi.android.example.service

import com.tapi.android.example.utils.Util
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {

    private val okHttpClientBuilder = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID " + Util.APPLICATION_ID)
                .build()
            chain.proceed(request)
        }

    private val okHttpClient = okHttpClientBuilder.build()

    fun getPhotosService(): UnSplashService{
        return Retrofit.Builder()
            .baseUrl(Util.BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnSplashService::class.java)
    }
}