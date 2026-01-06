package com.example.alpvp.data.repository

import com.example.alpvp.ui.model.User
import com.example.alpvp.data.service.ApiService

class NetworkUserRepository(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): List<User> = apiService.getUsers()
}