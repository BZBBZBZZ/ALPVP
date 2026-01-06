package com.example.alpvp.data.repository

import com.example.alpvp.ui.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}
