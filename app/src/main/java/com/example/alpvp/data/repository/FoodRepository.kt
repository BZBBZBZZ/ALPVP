package com.example.alpvp.data.repository

import com.example.alpvp.data.service.FoodServerService
import com.example.alpvp.ui.model.FoodDetailWrapper
import com.example.alpvp.ui.model.FoodWrapper
import retrofit2.Response

class FoodRepository(private val service: FoodServerService) {
    suspend fun getAllFoods(): Response<FoodWrapper> {
        return service.getAllFoods()
    }

    suspend fun getFoodById(id: Int): Response<FoodDetailWrapper> {
        return service.getFoodById(id)
    }
}