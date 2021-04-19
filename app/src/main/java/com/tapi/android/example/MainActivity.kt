package com.tapi.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapi.android.example.responsite.PhotosUnplashRes
import com.tapi.android.example.service.ApiClient
import com.tapi.android.example.service.UnSplashService
import com.tapi.android.example.service.models.RootObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}