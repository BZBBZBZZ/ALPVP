package com.example.alpvp.data.container


import com.example.alpvp.data.repository.QuizRepository
import com.example.alpvp.data.service.AuthService
import com.example.alpvp.data.service.LeaderboardService
import com.example.alpvp.data.service.QuizService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

class AppContainer {
    companion object {
        // ganti localhost pake 10.0.2.2 buat emulator, tapi g pake
        private const val BASE_URL = "http://10.0.2.2:3000/"
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val authService: AuthService by lazy { // <-- 2. ADD AuthService here
        retrofit.create(AuthService::class.java)
    }
    private val retrofitService: QuizService by lazy {
        retrofit.create(QuizService::class.java) //di create ama retrofit
    }

    val leaderboardService: LeaderboardService by lazy {
        retrofit.create(LeaderboardService::class.java)
    }


    val quizRepository: QuizRepository by lazy {
        QuizRepository(retrofitService)
    }
}