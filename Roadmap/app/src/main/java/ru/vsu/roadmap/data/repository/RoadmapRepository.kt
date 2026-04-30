package ru.vsu.roadmap.data.repository

import ru.vsu.roadmap.data.api.RoadmapApi
import ru.vsu.roadmap.data.model.RoadmapDto
import ru.vsu.roadmap.data.model.RoadmapStepDto
import ru.vsu.roadmap.data.model.UserRoadmapProgressDto
import ru.vsu.roadmap.data.model.UserStepProgressDto
import ru.vsu.roadmap.utils.TokenManager

class RoadmapRepository(
    private val api: RoadmapApi,
    private val tokenManager: TokenManager
) {
    private fun getBearerToken(): String {
        return "Bearer ${tokenManager.getToken() ?: ""}"
    }

    suspend fun getAllRoadmaps(): Result<List<RoadmapDto>> {
        return try {
            val roadmaps = api.getAllRoadmaps(getBearerToken())
            Result.success(roadmaps)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRoadmapById(id: Long): Result<RoadmapDto> {
        return try {
            val roadmap = api.getRoadmapById(getBearerToken(), id)
            Result.success(roadmap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRoadmapSteps(id: Long): Result<List<RoadmapStepDto>> {
        return try {
            val steps = api.getRoadmapSteps(getBearerToken(), id)
            Result.success(steps)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStepProgress(roadmapId: Long): Result<List<UserStepProgressDto>> {
        return try {
            Result.success(api.getStepProgress(getBearerToken(), roadmapId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavorites(): Result<List<RoadmapDto>> {
        return try {
            val favorites = api.getFavorites(getBearerToken())
            Result.success(favorites)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(id: Long): Result<Boolean> {
        return try {
            val response = api.toggleFavorite(getBearerToken(), id)
            Result.success(response["isFavorite"] ?: false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startRoadmap(id: Long): Result<Unit> {
        return try {
            api.startRoadmap(getBearerToken(), id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProgress(roadmapId: Long): Result<UserRoadmapProgressDto> {
        return try {
            val progress = api.getUserProgress(getBearerToken(), roadmapId)
            Result.success(progress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUserProgress(): Result<List<UserRoadmapProgressDto>> {
        return try {
            val progressList = api.getAllUserProgress(getBearerToken())
            Result.success(progressList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markStepCompleted(stepId: Long, isCompleted: Boolean): Result<Unit> {
        return try {
            api.markStepCompleted(getBearerToken(), stepId, mapOf("isCompleted" to isCompleted))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
