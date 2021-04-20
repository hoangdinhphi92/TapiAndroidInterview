package com.tapi.android.example.service

import com.tapi.android.example.data.Photo
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://api.unsplash.com"

interface UnSplashService {

    // Edit This
    @GET("/photos")
    suspend fun queryPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientId: String = "6fa91622109e859b1c40218a5dead99f7262cf4f698b1e2cb89dd18fc5824d15"
    ): List<Photo>

    companion object {
        private val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .build()
                chain.proceed(request)
            }

        private val okHttpClient = okHttpClientBuilder.build()

        val service by lazy {
             Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UnSplashService::class.java)
        }
    }
}

