package com.example.alpvp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alpvp.data.service.AuthService // Asumsi Anda punya ini
import com.example.alpvp.ui.model.LoginRequest
import com.example.alpvp.ui.model.RegisterRequest
import com.example.alpvp.ui.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State untuk UI
sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String?) : AuthUiState
}

class AuthViewModel(private val authService: AuthService) : ViewModel() {

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val response = authService.login(LoginRequest(username, password))
                Log.d("AuthViewModel", "Server Response: $response")

                // --- FIX IS HERE ---
                // Access the user object through the nested 'data' field.
                val user = response.data?.user

                if (user != null) {
                    // Pass the correctly extracted user object to the Success state
                    _authUiState.value = AuthUiState.Success(user)
                } else {
                    Log.e("AuthViewModel", "Login failed: User object was null in the response.")
                    // You might want a more specific error message here now
                    _authUiState.value = AuthUiState.Error("Login failed: Invalid data from server.")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "An exception occurred during login", e)
                _authUiState.value = AuthUiState.Error("Failed to connect or process the request.")
            }
        }
    }


    fun register(username: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val response = authService.register(RegisterRequest(username, password))
                if (response.success && response.user != null) {
                    _authUiState.value = AuthUiState.Success(response.user)
                } else {
                    _authUiState.value = AuthUiState.Error(response.message)
                }
            } catch (e: Exception) {
                _authUiState.value = AuthUiState.Error("Gagal terhubung ke server.")
            }
        }
    }
    
    // Fungsi untuk mereset state kembali ke Idle, bisa dipanggil setelah navigasi
    fun resetState() {
        _authUiState.value = AuthUiState.Idle
    }
}
