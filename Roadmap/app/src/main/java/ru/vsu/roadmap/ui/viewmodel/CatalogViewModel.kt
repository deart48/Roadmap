package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.data.repository.RoadmapRepository

import ru.vsu.roadmap.data.model.RoadmapStepDto

class CatalogViewModel(private val repository: RoadmapRepository) : ViewModel() {
    var roadmaps by mutableStateOf<List<RoadmapDto>>(emptyList())
    var favoriteIds by mutableStateOf<Set<Long>>(emptySet())
    var currentSteps by mutableStateOf<List<RoadmapStepDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            error = null
            
            // Fetch roadmaps
            val roadmapsResult = repository.getAllRoadmaps()
            if (roadmapsResult.isSuccess) {
                roadmaps = roadmapsResult.getOrDefault(emptyList())
            } else {
                error = roadmapsResult.exceptionOrNull()?.message
            }
            
            // Fetch favorites to know which ones are marked
            val favoritesResult = repository.getFavorites()
            if (favoritesResult.isSuccess) {
                val favs = favoritesResult.getOrDefault(emptyList())
                favoriteIds = favs.map { it.id }.toSet()
            }
            
            isLoading = false
        }
    }
    
    fun loadSteps(roadmapId: Long) {
        viewModelScope.launch {
            currentSteps = emptyList()
            val result = repository.getRoadmapSteps(roadmapId)
            if (result.isSuccess) {
                currentSteps = result.getOrDefault(emptyList())
            }
        }
    }

    fun startRoadmap(roadmapId: Long) {
        viewModelScope.launch {
            repository.startRoadmap(roadmapId)
        }
    }

    fun toggleFavorite(roadmap: RoadmapDto) {
        viewModelScope.launch {
            // Optimistic update
            val isCurrentlyFavorite = favoriteIds.contains(roadmap.id)
            if (isCurrentlyFavorite) {
                favoriteIds = favoriteIds - roadmap.id
            } else {
                favoriteIds = favoriteIds + roadmap.id
            }
            
            val result = repository.toggleFavorite(roadmap.id)
            
            if (result.isFailure) {
                // Revert on failure
                if (isCurrentlyFavorite) {
                    favoriteIds = favoriteIds + roadmap.id
                } else {
                    favoriteIds = favoriteIds - roadmap.id
                }
                error = result.exceptionOrNull()?.message
            }
        }
    }
}
