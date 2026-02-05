package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.repository.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isRegistered by mutableStateOf(false)

    fun onRegisterClick() {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            error = "Please fill all fields"
            return
        }
        
        viewModelScope.launch {
            isLoading = true
            error = null
            
            // Note: Current backend API register takes extended fields.
            // We pass available fields here. Full profile update happens in UserInfoScreen.
            val result = authRepository.register(
                email = email, 
                password = password,
                name = name,
                surname = "",
                birthDate = "",
                position = ""
            )
            if (result.isSuccess) {
                isRegistered = true
            } else {
                error = result.exceptionOrNull()?.message ?: "Registration failed"
            }
            
            isLoading = false
        }
    }
}