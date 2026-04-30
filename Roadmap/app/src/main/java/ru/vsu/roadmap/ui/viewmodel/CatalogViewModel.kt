package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.data.model.RoadmapStepDto
import ru.vsu.roadmap.data.repository.RoadmapRepository
import ru.vsu.roadmap.utils.SelectedRoadmapStore

class CatalogViewModel(
    private val repository: RoadmapRepository,
    private val selectedRoadmapStore: SelectedRoadmapStore,
) : ViewModel() {
    var roadmaps by mutableStateOf<List<RoadmapDto>>(emptyList())
    var favoriteIds by mutableStateOf<Set<Long>>(emptySet())
    /** Дорожные карты, по которым уже есть запись прогресса на сервере (можно «Продолжить»). */
    var startedRoadmapIds by mutableStateOf<Set<Long>>(emptySet())
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

            val roadmapsResult = repository.getAllRoadmaps()
            if (roadmapsResult.isSuccess) {
                roadmaps = roadmapsResult.getOrDefault(emptyList())
            } else {
                error = roadmapsResult.exceptionOrNull()?.message
            }

            val favoritesResult = repository.getFavorites()
            if (favoritesResult.isSuccess) {
                val favs = favoritesResult.getOrDefault(emptyList())
                favoriteIds = favs.map { it.id }.toSet()
            }

            refreshStartedRoadmaps()

            isLoading = false
        }
    }

    private suspend fun refreshStartedRoadmaps() {
        val progressResult = repository.getAllUserProgress()
        if (progressResult.isSuccess) {
            startedRoadmapIds =
                progressResult.getOrNull().orEmpty().map { it.roadmapId }.toSet()
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

    /**
     * Сохраняет выбор карты для вкладки Roadmap. Всегда вызывает [RoadmapRepository.startRoadmap], чтобы на сервере
     * в [user_roadmap_progress.in_progress] отражалась текущая активная карта (и при «Продолжить»).
     */
    fun selectRoadmapForViewer(
        roadmapId: Long,
        onComplete: () -> Unit,
    ) {
        viewModelScope.launch {
            repository.startRoadmap(roadmapId)
            selectedRoadmapStore.setSelectedRoadmapId(roadmapId)
            refreshStartedRoadmaps()
            onComplete()
        }
    }

    fun toggleFavorite(roadmap: RoadmapDto) {
        viewModelScope.launch {
            val isCurrentlyFavorite = favoriteIds.contains(roadmap.id)
            if (isCurrentlyFavorite) {
                favoriteIds = favoriteIds - roadmap.id
            } else {
                favoriteIds = favoriteIds + roadmap.id
            }

            val result = repository.toggleFavorite(roadmap.id)

            if (result.isFailure) {
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
