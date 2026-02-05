package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.data.model.UserProfileDto
import ru.vsu.roadmap.data.model.UserRoadmapProgressDto
import ru.vsu.roadmap.data.repository.RoadmapRepository
import ru.vsu.roadmap.data.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val roadmapRepository: RoadmapRepository
) : ViewModel() {
    var profile by mutableStateOf<UserProfileDto?>(null)
    var favorites by mutableStateOf<List<RoadmapDto>>(emptyList())
    var activeProgress by mutableStateOf<UserRoadmapProgressDto?>(null)
    var activeRoadmap by mutableStateOf<RoadmapDto?>(null) // The roadmap corresponding to activeProgress
    
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            error = null
            
            // Parallel loading
            val profileDeferred = async { userRepository.getProfile() }
            val favoritesDeferred = async { roadmapRepository.getFavorites() }
            val progressDeferred = async { roadmapRepository.getAllUserProgress() }
            
            val profileResult = profileDeferred.await()
            val favoritesResult = favoritesDeferred.await()
            val progressResult = progressDeferred.await()
            
            if (profileResult.isSuccess) {
                profile = profileResult.getOrNull()
            }
            
            if (favoritesResult.isSuccess) {
                favorites = favoritesResult.getOrDefault(emptyList())
            }
            
            if (progressResult.isSuccess) {
                val allProgress = progressResult.getOrDefault(emptyList())
                // Find the most recently updated progress or the one with highest %
                // API returns sorted by updatedAt DESC, so taking first.
                val latest = allProgress.firstOrNull()
                activeProgress = latest
                
                if (latest != null) {
                    // Fetch the roadmap details for this progress to show title
                    val roadmapResult = roadmapRepository.getRoadmapById(latest.roadmapId)
                    if (roadmapResult.isSuccess) {
                        activeRoadmap = roadmapResult.getOrNull()
                    }
                }
            } else {
                 // only show error if profile failed, others are secondary
                 if (profileResult.isFailure) error = profileResult.exceptionOrNull()?.message
            }
            
            isLoading = false
        }
    }
}
