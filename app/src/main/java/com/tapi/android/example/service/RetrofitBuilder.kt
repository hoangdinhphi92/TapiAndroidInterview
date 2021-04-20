package com.tapi.android.example.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://api.unsplash.com/"
    private const val CLIENT_ID ="6fa91622109e859b1c40218a5dead99f7262cf4f698b1e2cb89dd18fc5824d15"
    private fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(CLIENT_ID))
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val unSplashService: UnSplashService = getRetrofit().create(UnSplashService::class.java)
}

class HeaderInterceptor(val clientId: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().addHeader("Authorization", "Client-ID $clientId").build()
        return chain.proceed(request)
    }
}