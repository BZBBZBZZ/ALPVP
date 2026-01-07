package com.example.alpvp.data.service

import com.example.alpvp.data.dto.LeaderboardResponse
import com.example.alpvp.ui.model.User
import retrofit2.http.GET

interface LeaderboardService {
    // Asumsi endpoint /users mengembalikan daftar semua pengguna dengan skor mereka
    @GET("api/leaderboard")
    suspend fun getLeaderboard(): LeaderboardResponse
}