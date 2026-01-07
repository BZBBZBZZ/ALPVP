package com.example.alpvp.data.dto

import com.example.alpvp.ui.model.User
import com.google.gson.annotations.SerializedName

// This class matches the { "data": [...] } structure
data class LeaderboardResponse(
    // The @SerializedName MUST match the key in your server's JSON.
    // Common keys are "data", "users", "leaderboard", etc. Check your backend code.
    // I will assume it is "data" for this example.
    @SerializedName("data")
    val users: List<User>
)
