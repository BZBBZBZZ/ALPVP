package com.example.alpvp.data

import com.example.alpvp.data.service.AuthService
import com.example.alpvp.data.service.FoodServerService
import com.example.alpvp.data.service.LeaderboardService
import com.example.alpvp.data.service.QuizService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val leaderboardService: LeaderboardService by lazy {
        retrofit.create(LeaderboardService::class.java)
    }

    val foodServerService: FoodServerService by lazy {
        retrofit.create(FoodServerService::class.java)
    }

    val quizService: QuizService by lazy {
        retrofit.create(QuizService::class.java)
    }
}
