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

class RegisterViewModel(
    application: Application,
    private val authRepository: AuthRepository,
) : AndroidViewModel(application) {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var isRegistered by mutableStateOf(false)
        private set

    fun onEmailChange(value: String) {
        email = value
        if (error != null) error = null
    }

    fun onPasswordChange(value: String) {
        password = value
        if (error != null) error = null
    }

    fun onNameChange(value: String) {
        name = value
        if (error != null) error = null
    }

    fun onRegisterClick() {
        val ctx = getApplication<Application>()

        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            error = ctx.getString(R.string.error_empty_fields)
            return
        }
        if (name.trim().length < MIN_NAME_LENGTH) {
            error = ctx.getString(R.string.error_name_too_short)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            error = ctx.getString(R.string.error_invalid_email)
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null

            // Note: extended profile fields are filled in UserInfoScreen.
            val result = authRepository.register(
                email = email.trim(),
                password = password,
                name = name.trim(),
                surname = "",
                birthDate = "",
                position = "",
            )
            if (result.isSuccess) {
                isRegistered = true
            } else {
                error = ErrorMapper.mapRegisterError(result.exceptionOrNull(), ctx)
            }

            isLoading = false
        }
    }

    fun consumeRegistered() {
        isRegistered = false
    }

    private companion object {
        const val MIN_NAME_LENGTH = 2
    }
}
