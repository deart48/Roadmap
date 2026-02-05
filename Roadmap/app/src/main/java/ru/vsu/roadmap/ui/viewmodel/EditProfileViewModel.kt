package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.repository.UserRepository

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {
    var name by mutableStateOf("")
    var surname by mutableStateOf("") // Not supported by backend yet, keeping local
    var dob by mutableStateOf("") // Not supported by backend yet
    var position by mutableStateOf("") // Maps to 'about'
    var email by mutableStateOf("") 

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSaved by mutableStateOf(false)

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            val result = repository.getProfile()
            if (result.isSuccess) {
                val profile = result.getOrNull()
                profile?.let {
                    name = it.name ?: ""
                    surname = it.surname ?: ""
                    dob = it.birthDate ?: ""
                    position = it.about ?: ""
                    email = it.email
                }
            } else {
                error = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            isLoading = true
            isSaved = false
            // Mapping position to 'about'
            val result = repository.updateProfile(name, surname, dob, position, null)
            if (result.isSuccess) {
                isSaved = true
            } else {
                error = result.exceptionOrNull()?.message
            }
            isLoading = false
        }
    }
}
