package com.tapi.android.example.service

import com.tapi.android.example.data.Photo
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


const val BASE_URL = "https://api.unsplash.com/"
private const val CLIENT_ID = "cKakzKM1cx44BUYBnEIrrgN_gnGqt81UcE7GstJEils"


val client = OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS)
    .hostnameVerifier { _, _ -> true }.build()

val mRetrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
    .addConverterFactory(GsonConverterFactory.create()).build()


object APIService {
    val retrofit: UnSplashService by lazy { mRetrofit.create(UnSplashService::class.java) }
}

interface UnSplashService {
    @GET("/photos/")
    suspend fun queryPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 12,
        @Query("client_id") id: String = CLIENT_ID

    ): Response<List<Photo>>

}