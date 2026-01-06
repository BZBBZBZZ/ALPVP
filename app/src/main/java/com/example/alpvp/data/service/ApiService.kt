package com.example.alpvp.data.service

import com.example.alpvp.ui.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}