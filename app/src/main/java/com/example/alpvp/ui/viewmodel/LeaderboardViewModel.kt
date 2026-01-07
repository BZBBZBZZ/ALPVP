package com.example.alpvp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.data.service.LeaderboardService
import com.example.alpvp.ui.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed interface LeaderboardUiState {
    object Loading : LeaderboardUiState
    data class Success(val users: List<User>) : LeaderboardUiState
    data class Error(val message: String) : LeaderboardUiState
}

class LeaderboardViewModel(private val leaderboardService: LeaderboardService) : ViewModel() {

    private val _leaderboardUiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val leaderboardUiState: StateFlow<LeaderboardUiState> = _leaderboardUiState.asStateFlow()

    init {
        fetchLeaderboard()
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _leaderboardUiState.value = LeaderboardUiState.Loading
            try {
                Log.d("LeaderboardViewModel", "Attempting to fetch leaderboard...")

                // --- FIX IS HERE: Access the list through the wrapper ---
                val response = leaderboardService.getLeaderboard()
                val users = response.users // <-- Unwrap the list here

                Log.d("LeaderboardViewModel", "Successfully fetched ${users.size} users.")

                val sortedUsers = users.sortedWith(
                    compareByDescending<User> { it.high_score }
                        .thenBy { parseDate(it.last_played_at) }
                )
                _leaderboardUiState.value = LeaderboardUiState.Success(sortedUsers)

            } catch (e: Exception) {
                Log.e("LeaderboardViewModel", "Failed to fetch leaderboard", e)
                _leaderboardUiState.value = LeaderboardUiState.Error("Gagal memuat leaderboard: ${e.message}")
            }
        }
    }


    private fun parseDate(dateString: String?): LocalDateTime? {
        return try {
            dateString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
        } catch (e: Exception) {
            null
        }
    }
}
