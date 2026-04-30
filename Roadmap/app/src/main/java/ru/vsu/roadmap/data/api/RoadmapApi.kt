package ru.vsu.roadmap.data.api

import retrofit2.http.*
import ru.vsu.roadmap.data.model.*

interface RoadmapApi {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    // User
    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfileDto

    @PUT("profile")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body request: UpdateProfileRequest): UserProfileDto

    // Roadmaps
    @GET("roadmaps")
    suspend fun getAllRoadmaps(@Header("Authorization") token: String): List<RoadmapDto>

    @GET("roadmaps/{id}")
    suspend fun getRoadmapById(@Header("Authorization") token: String, @Path("id") id: Long): RoadmapDto

    @GET("roadmaps/{id}/steps")
    suspend fun getRoadmapSteps(@Header("Authorization") token: String, @Path("id") id: Long): List<RoadmapStepDto>

    @GET("roadmaps/{id}/steps/progress")
    suspend fun getStepProgress(
        @Header("Authorization") token: String,
        @Path("id") roadmapId: Long,
    ): List<UserStepProgressDto>

    @GET("roadmaps/favorites")
    suspend fun getFavorites(@Header("Authorization") token: String): List<RoadmapDto>

    @POST("roadmaps/{id}/favorite")
    suspend fun toggleFavorite(@Header("Authorization") token: String, @Path("id") id: Long): Map<String, Boolean>

    @POST("roadmaps/{id}/start")
    suspend fun startRoadmap(@Header("Authorization") token: String, @Path("id") id: Long)

    @GET("roadmaps/progress")
    suspend fun getAllUserProgress(@Header("Authorization") token: String): List<UserRoadmapProgressDto>

    @GET("roadmaps/{id}/progress")
    suspend fun getUserProgress(@Header("Authorization") token: String, @Path("id") id: Long): UserRoadmapProgressDto

    @POST("roadmaps/steps/{stepId}/complete")
    suspend fun markStepCompleted(
        @Header("Authorization") token: String, 
        @Path("stepId") stepId: Long,
        @Body status: Map<String, Boolean>
    )
}
