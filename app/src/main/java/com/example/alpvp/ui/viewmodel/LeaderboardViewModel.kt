package com.example.alpvp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.ui.model.User
import com.example.alpvp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "LeaderboardViewModel"

sealed interface LeaderboardUiState {
    data class Success(val users: List<User>) : LeaderboardUiState
    data class Error(val message: String) : LeaderboardUiState
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
                Log.d(TAG, "Fetching users from repository...")
                val users = userRepository.getUsers()
                Log.d(TAG, "Successfully fetched ${users.size} users")
                // Sort users by score in descending order
                val sortedUsers = users.sortedByDescending { it.score }
                Log.d(TAG, "Leaderboard sorted by score (descending)")
                LeaderboardUiState.Success(sortedUsers)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching users: ${e.message}", e)
                Log.e(TAG, "Exception type: ${e.javaClass.simpleName}")
                e.stackTrace.forEach {
                    Log.e(TAG, "  at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})")
                }
                LeaderboardUiState.Error("Failed to load leaderboard: ${e.message}")
            }
        }
    }
}