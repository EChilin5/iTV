package com.example.itv.api

import com.example.itv.model.Post
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi   {
    @GET("posts/")
    suspend fun getPost(): Response<Post>

}