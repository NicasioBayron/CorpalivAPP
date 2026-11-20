package com.example.corpalivapp.data.remote

import com.example.corpalivapp.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET(value = "/posts")
    suspend fun getPosts(): List<Post>
}
