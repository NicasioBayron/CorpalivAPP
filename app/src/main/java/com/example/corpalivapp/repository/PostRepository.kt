package com.example.corpalivapp.repository

import com.example.corpalivapp.data.model.Post
import com.example.corpalivapp.data.remote.RetrofitInstance

class PostRepository {
    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}
