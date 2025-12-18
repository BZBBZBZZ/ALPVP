package com.example.alpvp.data.service

import com.example.alpvp.ui.model.FoodDetailWrapper
import com.example.alpvp.ui.model.FoodWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodServerService {
    @GET("api/foods")
    suspend fun getAllFoods(): Response<FoodWrapper>

    @GET("api/foods/{id}")
    suspend fun getFoodById(@Path("id") id: Int): Response<FoodDetailWrapper>
}