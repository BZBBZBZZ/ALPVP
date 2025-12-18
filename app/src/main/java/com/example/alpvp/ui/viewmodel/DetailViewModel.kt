package com.example.alpvp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.data.container.FoodAppContainer
import com.example.alpvp.ui.uistate.DetailUIState
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repository = FoodAppContainer().foodRepository

    var detailUIState: DetailUIState by mutableStateOf(DetailUIState.Start)
        private set

    fun getFoodById(id: Int) {
        viewModelScope.launch {
            detailUIState = DetailUIState.Loading
            try {
                val response = repository.getFoodById(id)
                if (response.isSuccessful) {
                    detailUIState = DetailUIState.Success(response.body()!!.data)
                } else {
                    detailUIState = DetailUIState.Error("Detail tidak ditemukan")
                }
            } catch (e: Exception) {
                detailUIState = DetailUIState.Error(e.message ?: "Error koneksi")
            }
        }
    }
}