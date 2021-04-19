package com.tapi.android.example.service

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(private val clientId: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val auth = "Client-ID $clientId"
        var request = chain.request()
        request = request.newBuilder()
                .addHeader("Authorization", auth)
                .build()
        return chain.proceed(request)
    }
}