package com.example.itv.repository

import com.example.itv.api.RetrofitInstance
import com.example.itv.model.Post
import retrofit2.Response
import retrofit2.Retrofit

class Repository {

    suspend fun getPost(): Response<Post> {
        return RetrofitInstance.api.getPost()
    }
}