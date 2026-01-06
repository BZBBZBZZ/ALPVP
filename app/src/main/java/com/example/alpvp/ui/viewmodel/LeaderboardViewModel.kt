package com.example.alpvp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.ui.model.User
import com.example.alpvp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface LeaderboardUiState {
    data class Success(val users: List<User>) : LeaderboardUiState
    object Error : LeaderboardUiState
    object Loading : LeaderboardUiState
}

class LeaderboardViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        getUsers()
    }

    fun getUsers() {
        viewModelScope.launch {
            _uiState.value = LeaderboardUiState.Loading
            _uiState.value = try {
                LeaderboardUiState.Success(userRepository.getUsers())
            } catch (e: IOException) {
                // Tambahkan penanganan kesalahan lain jika perlu
                LeaderboardUiState.Error
            }
        }
    }
}