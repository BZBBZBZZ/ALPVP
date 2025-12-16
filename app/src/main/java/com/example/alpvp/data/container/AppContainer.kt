package com.example.alpvp.data.container


import com.example.alpvp.data.repository.QuizRepository
import com.example.alpvp.data.service.QuizService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    companion object {
        // Ganti localhost dengan 10.0.2.2 untuk Emulator Android
        private const val BASE_URL = "http://10.0.2.2:3000/"
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: QuizService by lazy {
        retrofit.create(QuizService::class.java)
    }

    val quizRepository: QuizRepository by lazy {
        QuizRepository(retrofitService)
    }
}