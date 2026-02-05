package ru.vsu.roadmap.data.repository

import ru.vsu.roadmap.data.api.RoadmapApi
import ru.vsu.roadmap.data.model.UpdateProfileRequest
import ru.vsu.roadmap.data.model.UserProfileDto
import ru.vsu.roadmap.utils.TokenManager

class UserRepository(
    private val api: RoadmapApi,
    private val tokenManager: TokenManager
) {
    private fun getBearerToken(): String {
        return "Bearer ${tokenManager.getToken() ?: ""}"
    }

    suspend fun getProfile(): Result<UserProfileDto> {
        return try {
            val profile = api.getProfile(getBearerToken())
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        name: String?, 
        surname: String?, 
        birthDate: String?, 
        about: String?, 
        avatarUrl: String?
    ): Result<UserProfileDto> {
        return try {
            val request = UpdateProfileRequest(name, surname, birthDate, about, avatarUrl)
            val profile = api.updateProfile(getBearerToken(), request)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
