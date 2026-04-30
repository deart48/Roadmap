package ru.vsu.roadmap.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.R
import ru.vsu.roadmap.data.repository.AuthRepository
import ru.vsu.roadmap.utils.ErrorMapper

class LoginViewModel(
    application: Application,
    private val authRepository: AuthRepository,
) : AndroidViewModel(application) {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var isLoggedIn by mutableStateOf(false)
        private set

    fun onEmailChange(value: String) {
        email = value
        if (error != null) error = null
    }

    fun onPasswordChange(value: String) {
        password = value
        if (error != null) error = null
    }

    fun onLoginClick() {
        val ctx = getApplication<Application>()

        if (email.isBlank() || password.isBlank()) {
            error = ctx.getString(R.string.error_empty_fields)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            error = ctx.getString(R.string.error_invalid_email)
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null

            val result = authRepository.login(email.trim(), password)
            if (result.isSuccess) {
                isLoggedIn = true
            } else {
                error = ErrorMapper.mapAuthError(result.exceptionOrNull(), ctx)
            }

            isLoading = false
        }
    }

    fun consumeLoggedIn() {
        isLoggedIn = false
    }
}
