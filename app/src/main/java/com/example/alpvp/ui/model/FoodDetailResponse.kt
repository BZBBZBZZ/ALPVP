package com.example.alpvp.ui.model

data class FoodDetailResponse(
    val id: Int,
    val name: String,
    val category: String,
    val image_url: String,
    val short_desc: String,
    val food_detail_desc: String
)