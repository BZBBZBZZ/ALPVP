package com.example.alpvp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alpvp.data.container.AppContainer

// Factory ini sedikit lebih umum dan bisa diperluas untuk ViewModel lain.
class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(container.userRepository) as T
        }
        // Tambahkan ViewModel lain di sini jika ada
        // contoh:
        // if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
        //     ...
        // }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}