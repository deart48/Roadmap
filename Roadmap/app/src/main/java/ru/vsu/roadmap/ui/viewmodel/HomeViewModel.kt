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

class HomeViewModel(
    private val roadmapRepository: RoadmapRepository,
    private val selectedRoadmapStore: SelectedRoadmapStore,
) : ViewModel() {
    var roadmaps by mutableStateOf<List<RoadmapDto>>(emptyList())
    var favorites by mutableStateOf<List<RoadmapDto>>(emptyList())
    /** Дорожные карты с записью прогресса на сервере — для подписи «Продолжить». */
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

            refreshStartedRoadmaps()

            isLoading = false
        }
    }

    private suspend fun refreshStartedRoadmaps() {
        val progressResult = roadmapRepository.getAllUserProgress()
        if (progressResult.isSuccess) {
            startedRoadmapIds =
                progressResult.getOrNull().orEmpty().map { it.roadmapId }.toSet()
        }
    }

    /**
     * Обновляет только список избранного (без перетасовки блока «Возможно вам подойдёт»).
     * Вызывается при каждом возврате на главный экран, чтобы изменения из каталога/профиля
     * сразу отражались здесь.
     */
    fun refreshFavorites() {
        viewModelScope.launch {
            val favoritesResult = roadmapRepository.getFavorites()
            if (favoritesResult.isSuccess) {
                favorites = favoritesResult.getOrDefault(emptyList())
            }
        }
    }

    fun loadSteps(roadmapId: Long) {
        viewModelScope.launch {
            currentSteps = emptyList()
            val result = roadmapRepository.getRoadmapSteps(roadmapId)
            if (result.isSuccess) {
                currentSteps = result.getOrDefault(emptyList())
            }
        }
    }

    fun selectRoadmapForViewer(roadmapId: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            roadmapRepository.startRoadmap(roadmapId)
            selectedRoadmapStore.setSelectedRoadmapId(roadmapId)
            refreshStartedRoadmaps()
            onComplete()
        }
    }
}
