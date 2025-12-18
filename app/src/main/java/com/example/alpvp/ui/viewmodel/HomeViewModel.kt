package com.example.alpvp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.data.container.FoodAppContainer
import com.example.alpvp.ui.uistate.HomeUIState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = FoodAppContainer().foodRepository

    var homeUIState: HomeUIState by mutableStateOf(HomeUIState.Start)
        private set

    init {
        getAllFoods()
    }

    fun getAllFoods() {
        viewModelScope.launch {
            homeUIState = HomeUIState.Loading
            try {
                val response = repository.getAllFoods()
                if (response.isSuccessful) {
                    homeUIState = HomeUIState.Success(response.body()!!.data)
                } else {
                    homeUIState = HomeUIState.Error("Gagal mengambil data")
                }
            } catch (e: Exception) {
                homeUIState = HomeUIState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}