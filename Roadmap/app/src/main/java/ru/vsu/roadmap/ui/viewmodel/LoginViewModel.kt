package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.repository.AuthRepository

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(false)

    fun onLoginClick() {
        if (email.isBlank() || password.isBlank()) {
            error = "Please fill all fields"
            return
        }
        
        viewModelScope.launch {
            isLoading = true
            error = null
            
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                isLoggedIn = true
            } else {
                error = result.exceptionOrNull()?.message ?: "Login failed"
            }
            
            isLoading = false
        }
    }
}
