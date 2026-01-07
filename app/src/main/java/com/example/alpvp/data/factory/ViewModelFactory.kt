package com.example.alpvp.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alpvp.data.container.AppContainer
import com.example.alpvp.ui.viewmodel.AuthViewModel // <-- 1. IMPORT AuthViewModel
import com.example.alpvp.ui.viewmodel.LeaderboardViewModel

class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // --- 2. ADD THIS BLOCK to handle AuthViewModel ---
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(appContainer.authService) as T
        }
        // ----------------------------------------------------

        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(appContainer.leaderboardService) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
