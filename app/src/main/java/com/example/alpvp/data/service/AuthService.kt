package com.example.alpvp.data.service

import com.example.alpvp.data.dto.LoginResponse
import com.example.alpvp.ui.model.AuthResponse
import com.example.alpvp.ui.model.LoginRequest
import com.example.alpvp.ui.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/auth/login") // Sesuaikan dengan endpoint login di backend Anda
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register") // Sesuaikan dengan endpoint register di backend Anda
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}