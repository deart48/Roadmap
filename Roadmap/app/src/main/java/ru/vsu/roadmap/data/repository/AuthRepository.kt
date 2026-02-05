package ru.vsu.roadmap.data.repository

import ru.vsu.roadmap.data.api.RoadmapApi
import ru.vsu.roadmap.data.model.AuthResponse
import ru.vsu.roadmap.data.model.LoginRequest
import ru.vsu.roadmap.data.model.RegisterRequest
import ru.vsu.roadmap.utils.TokenManager

class AuthRepository(
    private val api: RoadmapApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            tokenManager.saveToken(response.token)
            tokenManager.saveUserId(response.user.id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String, 
        password: String,
        name: String,
        surname: String,
        birthDate: String,
        position: String
    ): Result<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(email, password, name, surname, birthDate, position))
            tokenManager.saveToken(response.token)
            tokenManager.saveUserId(response.user.id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }
    
    fun logout() {
        tokenManager.clearToken()
    }
}
