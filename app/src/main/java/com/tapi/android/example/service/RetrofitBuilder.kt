package com.tapi.android.example.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://api.unsplash.com/"
    private const val CLIENT_ID ="HhcTn-IhaEPDgYQ9IDwtg5DVPZGFJTos7vS1twuoTnQ"
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