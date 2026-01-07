package com.example.alpvp.data.repository

import android.util.Log
import com.example.alpvp.ui.model.User
import com.example.alpvp.data.service.ApiService

private const val TAG = "NetworkUserRepository"

class NetworkUserRepository(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return try {
            Log.d(TAG, "Calling API getUsers()...")
            val users = apiService.getUsers()
            Log.d(TAG, "API call successful, received ${users.size} users")
            users
        } catch (e: Exception) {
            Log.e(TAG, "API call failed: ${e.message}", e)
            throw e
        }
    }
}