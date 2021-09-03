package com.example.itv.api

import com.example.itv.api.RetrofitInstance.retrofit
import com.example.itv.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// make retrofit instance a singleton
object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(Constants.base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }
}