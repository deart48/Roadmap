package ru.vsu.roadmap.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.data.model.RoadmapStepDto
import ru.vsu.roadmap.data.model.UserProfileDto
import ru.vsu.roadmap.data.model.UserRoadmapProgressDto
import ru.vsu.roadmap.data.repository.RoadmapRepository
import ru.vsu.roadmap.data.repository.UserRepository
import ru.vsu.roadmap.utils.SelectedRoadmapStore

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val roadmapRepository: RoadmapRepository,
    private val selectedRoadmapStore: SelectedRoadmapStore,
) : ViewModel() {
    var profile by mutableStateOf<UserProfileDto?>(null)
    var favorites by mutableStateOf<List<RoadmapDto>>(emptyList())
    var activeProgress by mutableStateOf<UserRoadmapProgressDto?>(null)
    var activeRoadmap by mutableStateOf<RoadmapDto?>(null)

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
                startedRoadmapIds = allProgress.map { it.roadmapId }.toSet()
                val latest = allProgress.firstOrNull()
                activeProgress = latest

                if (latest != null) {
                    val roadmapResult = roadmapRepository.getRoadmapById(latest.roadmapId)
                    if (roadmapResult.isSuccess) {
                        activeRoadmap = roadmapResult.getOrNull()
                    }
                }
            } else {
                if (profileResult.isFailure) error = profileResult.exceptionOrNull()?.message
            }

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
