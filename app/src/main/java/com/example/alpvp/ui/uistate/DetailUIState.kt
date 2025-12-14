package com.example.alpvp.ui.uistate

import com.example.alpvp.ui.model.FoodDetailResponse

sealed interface DetailUIState {
    data class Success(val food: FoodDetailResponse) : DetailUIState
    data class Error(val message: String) : DetailUIState
    object Loading : DetailUIState
    object Start : DetailUIState
}