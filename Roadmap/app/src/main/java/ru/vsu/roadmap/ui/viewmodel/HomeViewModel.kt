package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.data.repository.RoadmapRepository

class HomeViewModel(private val roadmapRepository: RoadmapRepository) : ViewModel() {
    var roadmaps by mutableStateOf<List<RoadmapDto>>(emptyList())
    var favorites by mutableStateOf<List<RoadmapDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            error = null
            
            val roadmapsResult = roadmapRepository.getAllRoadmaps()
            if (roadmapsResult.isSuccess) {
                val allRoadmaps = roadmapsResult.getOrDefault(emptyList())
                roadmaps = allRoadmaps.shuffled().take(4)
            } else {
                 error = roadmapsResult.exceptionOrNull()?.message
            }
            
            val favoritesResult = roadmapRepository.getFavorites()
            if (favoritesResult.isSuccess) {
                favorites = favoritesResult.getOrDefault(emptyList())
            }
            
            isLoading = false
        }
    }
}
