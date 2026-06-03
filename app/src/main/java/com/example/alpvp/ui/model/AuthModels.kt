package com.example.alpvp.ui.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val password: String)

data class User(
    val user_id: Int,
    val username: String,
    val high_score: Int?,
    val last_played_at: String? // Assuming this will be a String in ISO 8601 format e.g., "2023-10-27T10:00:00Z"
)

data class AuthData(
    @SerializedName("token")
    val token: String?,

    @SerializedName("user")
    val user: User?
)

data class AuthResponse(
    @SerializedName("data")
    val data: AuthData?
)
