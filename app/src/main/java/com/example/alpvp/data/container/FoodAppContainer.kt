package com.example.alpvp.data.container

import com.example.alpvp.data.repository.FoodRepository
import com.example.alpvp.data.service.FoodServerService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodAppContainer {
    companion object {
        val BASE_URL = "http://10.0.2.2:3000/"
    }

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    private val retrofitService: FoodServerService by lazy {
        retrofit.create(FoodServerService::class.java)
    }

    val foodRepository: FoodRepository by lazy {
        FoodRepository(retrofitService)
    }
}