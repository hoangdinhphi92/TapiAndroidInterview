package com.tapi.android.example.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val client_id = "cKakzKM1cx44BUYBnEIrrgN_gnGqt81UcE7GstJEils"
    const val BASE_URL = "https://api.unsplash.com/"
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = createRetrofitClient()
            }
            return retrofit
        }


    private fun createRetrofitClient(): Retrofit {
        val builder = Retrofit.Builder()
        val headerInterceptor = HeaderInterceptor(client_id)

        val client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)

        return builder
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
}