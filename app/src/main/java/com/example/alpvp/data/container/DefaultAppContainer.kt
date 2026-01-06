package com.example.alpvp.data.container

import com.example.alpvp.data.repository.NetworkUserRepository
import com.example.alpvp.data.repository.UserRepository
import com.example.alpvp.data.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "http://10.0.2.2:3000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(retrofitService)
    }
}
