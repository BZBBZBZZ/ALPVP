package com.example.alpvp.data.dto

// In a file like LoginResponse.kt

import com.example.alpvp.ui.model.User
import com.google.gson.annotations.SerializedName

// 1. The main response class that wraps everything
// It should match what your AuthService.login() function returns
data class LoginResponse(
    @SerializedName("data")
    val data: ResponseData? // This links to the class below
)

// 2. This class represents the object nested inside "data"
data class ResponseData(
    @SerializedName("token")
    val token: String?,

    @SerializedName("user")
    val user: User? // This is your existing User model
)
