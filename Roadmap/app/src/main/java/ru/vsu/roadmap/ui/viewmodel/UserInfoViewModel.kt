package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.repository.UserRepository

class UserInfoViewModel(private val userRepository: UserRepository) : ViewModel() {
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var dob by mutableStateOf("")
    var about by mutableStateOf("") // using this for position
    var avatarUrl by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var isSaved by mutableStateOf(false)

    fun onSaveClick() {
        viewModelScope.launch {
            isLoading = true
            val result = userRepository.updateProfile(name, surname, dob, about, null)
            if (result.isSuccess) {
                isSaved = true
            }
            isLoading = false
        }
    }
}
