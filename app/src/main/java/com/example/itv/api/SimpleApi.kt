package com.example.itv.api

import com.example.itv.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi   {
    @GET("posts/2")
    suspend fun getPost(): Response<Post>

}