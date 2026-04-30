package ru.vsu.roadmap.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vsu.roadmap.R
import ru.vsu.roadmap.config.RoadmapScreenConfig
import ru.vsu.roadmap.data.model.RoadmapStepDto
import ru.vsu.roadmap.data.model.UserRoadmapProgressDto
import ru.vsu.roadmap.data.repository.RoadmapRepository
import ru.vsu.roadmap.utils.ErrorMapper
import ru.vsu.roadmap.utils.SelectedRoadmapStore

data class RoadmapUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    /** Пользователь ещё не выбрал дорожную карту в каталоге. */
    val needsCatalogSelection: Boolean = false,
    /** Сервер вернул число шагов ≠ [RoadmapScreenConfig.EXPECTED_STEP_COUNT]. */
    val stepCountMismatch: Boolean = false,
    val roadmapTitle: String = "",
    val steps: List<RoadmapStepDto> = emptyList(),
    val studiedStepIds: Set<Long> = emptySet(),
    val currentStepId: Long? = null,
)

class RoadmapViewModel(
    application: Application,
    private val roadmapRepository: RoadmapRepository,
    private val selectedRoadmapStore: SelectedRoadmapStore,
) : AndroidViewModel(application) {

    var uiState by mutableStateOf(RoadmapUiState())
        private set

    fun load() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, stepCountMismatch = false, needsCatalogSelection = false)
            val ctx = getApplication<Application>()
            var id = selectedRoadmapStore.getSelectedRoadmapId()
            if (id == null) {
                val progressRes = roadmapRepository.getAllUserProgress()
                if (progressRes.isSuccess) {
                    val resolved = resolveRoadmapIdFromServerProgress(progressRes.getOrNull().orEmpty())
                    if (resolved != null) {
                        id = resolved
                        selectedRoadmapStore.setSelectedRoadmapId(resolved)
                    }
                }
            }
            if (id == null) {
                uiState = RoadmapUiState(
                    isLoading = false,
                    needsCatalogSelection = true,
                )
                return@launch
            }

            val roadmapRes = roadmapRepository.getRoadmapById(id)
            val stepsRes = roadmapRepository.getRoadmapSteps(id)
            val progressRes = roadmapRepository.getStepProgress(id)

            val err = roadmapRes.exceptionOrNull()
                ?: stepsRes.exceptionOrNull()
                ?: progressRes.exceptionOrNull()
            if (err != null) {
                uiState = RoadmapUiState(
                    isLoading = false,
                    error = ErrorMapper.mapAuthError(err, ctx),
                    needsCatalogSelection = false,
                )
                return@launch
            }

            val roadmap = roadmapRes.getOrNull()
            if (roadmap == null) {
                uiState = RoadmapUiState(
                    isLoading = false,
                    error = ctx.getString(R.string.error_unknown),
                )
                return@launch
            }

            val steps = stepsRes.getOrNull().orEmpty().sortedBy { it.orderIndex }
            val progress = progressRes.getOrNull().orEmpty()

            val mismatch = steps.size != RoadmapScreenConfig.EXPECTED_STEP_COUNT
            val completedIds = progress.filter { it.isCompleted }.map { it.stepId }.toSet()
            val currentId = computeCurrentStepId(steps, completedIds)

            uiState = RoadmapUiState(
                isLoading = false,
                error = null,
                needsCatalogSelection = false,
                stepCountMismatch = mismatch,
                roadmapTitle = roadmap.title,
                steps = steps,
                studiedStepIds = completedIds,
                currentStepId = currentId,
            )
        }
    }

    fun toggleStepCompleted(stepId: Long, currentlyCompleted: Boolean) {
        viewModelScope.launch {
            val ctx = getApplication<Application>()
            uiState = uiState.copy(error = null)
            val result = roadmapRepository.markStepCompleted(stepId, !currentlyCompleted)
            if (result.isSuccess) {
                val newSet =
                    if (!currentlyCompleted) uiState.studiedStepIds + stepId
                    else uiState.studiedStepIds - stepId
                val currentId = computeCurrentStepId(uiState.steps, newSet)
                uiState = uiState.copy(
                    studiedStepIds = newSet,
                    currentStepId = currentId,
                )
            } else {
                uiState = uiState.copy(error = ErrorMapper.mapAuthError(result.exceptionOrNull(), ctx))
            }
        }
    }

    private fun computeCurrentStepId(steps: List<RoadmapStepDto>, completed: Set<Long>): Long? {
        val sorted = steps.sortedBy { it.orderIndex }
        return sorted.firstOrNull { it.id !in completed }?.id ?: sorted.lastOrNull()?.id
    }

    /**
     * После нового входа локальный выбор пуст; берём с сервера дорожную карту с [UserRoadmapProgressDto.inProgress],
     * иначе последнюю по [UserRoadmapProgressDto.updatedAt] среди начатых.
     */
    private fun resolveRoadmapIdFromServerProgress(progress: List<UserRoadmapProgressDto>): Long? {
        progress.firstOrNull { it.inProgress }?.let { return it.roadmapId }
        if (progress.isEmpty()) return null
        return progress.maxWith(compareBy({ it.updatedAt })).roadmapId
    }
}
