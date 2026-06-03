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
import retrofit2.Response

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

    var currentUser by mutableStateOf<User?>(null)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val response = authService.login(LoginRequest(username, password))
                Log.d("AuthViewModel", "Server Response: $response")

                if (response.isSuccessful) {
                    val user = response.body()?.data?.user
                    if (user != null) {
                        currentUser = user
                        _authUiState.value = AuthUiState.Success(user)
                    } else {
                        Log.e("AuthViewModel", "Login failed: User object was null in the response body.")
                        _authUiState.value = AuthUiState.Error("Login gagal: data user tidak valid.")
                    }
                } else {
                    _authUiState.value = AuthUiState.Error(parseErrorMessage(response))
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "An exception occurred during login", e)
                _authUiState.value = AuthUiState.Error("Gagal terhubung atau memproses request login.")
            }
        }
    }


    fun register(username: String, password: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            try {
                val response = authService.register(RegisterRequest(username, password))
                Log.d("AuthViewModel", "Register response: $response")

                if (response.isSuccessful) {
                    val user = response.body()?.data?.user
                    if (user != null) {
                        currentUser = user
                        _authUiState.value = AuthUiState.Success(user)
                    } else {
                        _authUiState.value = AuthUiState.Error("Registrasi gagal: data user dari server kosong.")
                    }
                } else {
                    _authUiState.value = AuthUiState.Error(parseErrorMessage(response))
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "An exception occurred during register", e)
                _authUiState.value = AuthUiState.Error("Gagal terhubung ke server.")
            }
        }
    }

    private fun parseErrorMessage(response: Response<*>): String {
        val raw = response.errorBody()?.string().orEmpty()
        if (raw.isBlank()) {
            return "Registrasi gagal: server tidak mengirim pesan error."
        }

        val regex = """"errors"\s*:\s*"([^"]+)""".toRegex()
        val message = regex.find(raw)?.groupValues?.getOrNull(1)
        return message ?: raw
    }

    // Fungsi untuk mereset state kembali ke Idle, bisa dipanggil setelah navigasi
    fun resetState() {
        _authUiState.value = AuthUiState.Idle
    }

    fun logout() {
        currentUser = null
        resetState()
    }
}
