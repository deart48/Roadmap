package ru.vsu.roadmap.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val email: String,
    val createdAt: String
)

@Serializable
data class UserProfileDto(
    val userId: Long,
    val email: String,
    val name: String?,
    val surname: String?,
    val birthDate: String?,
    val about: String?,
    val avatarUrl: String?
)

@Serializable
data class RoadmapDto(
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val createdAt: String
)

@Serializable
data class RoadmapStepDto(
    val id: Long,
    val roadmapId: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val orderIndex: Int,
    val links: List<StepLinkDto> = emptyList()
)

@Serializable
data class StepLinkDto(
    val id: Long,
    val stepId: Long,
    val title: String?,
    val url: String
)

@Serializable
data class UserRoadmapProgressDto(
    val userId: Long,
    val roadmapId: Long,
    val progressPercent: Int,
    val lastStepId: Long?,
    val updatedAt: String,
    val inProgress: Boolean
)

@Serializable
data class UserStepProgressDto(
    val userId: Long,
    val stepId: Long,
    val isCompleted: Boolean,
    val completedAt: String?
)

// Auth Requests
@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(
    val email: String, 
    val password: String,
    val name: String? = null,
    val surname: String? = null,
    val birthDate: String? = null,
    val about: String? = null
)

@Serializable
data class AuthResponse(val token: String, val user: UserDto)

// Roadmap Requests
@Serializable
data class CreateRoadmapRequest(
    val title: String,
    val description: String?,
    val imageUrl: String?
)

@Serializable
data class UpdateProfileRequest(
    val name: String?,
    val surname: String?,
    val birthDate: String?,
    val about: String?,
    val avatarUrl: String?
)
