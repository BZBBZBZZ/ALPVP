package com.example.alpvp.ui.uistate

import com.example.alpvp.ui.model.FoodResponse

sealed interface HomeUIState {
    data class Success(val foods: List<FoodResponse>) : HomeUIState
    data class Error(val message: String) : HomeUIState
    object Loading : HomeUIState
    object Start : HomeUIState
}