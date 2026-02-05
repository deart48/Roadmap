package ru.vsu.services

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.vsu.config.DatabaseFactory.dbQuery
import ru.vsu.models.*
import java.time.LocalDateTime

class RoadmapService {

    suspend fun getAllRoadmaps(): List<RoadmapDto> = dbQuery {
        Roadmaps.selectAll().map { toRoadmapDto(it) }
    }

    suspend fun getRoadmapById(id: Long): RoadmapDto? = dbQuery {
        Roadmaps.selectAll().where { Roadmaps.id eq id }.singleOrNull()?.let { toRoadmapDto(it) }
    }

    suspend fun getRoadmapSteps(roadmapId: Long): List<RoadmapStepDto> = dbQuery {
        val steps = RoadmapSteps.selectAll()
            .where { RoadmapSteps.roadmapId eq roadmapId }
            .orderBy(RoadmapSteps.orderIndex)
            .map { toRoadmapStepDto(it) }
        
        // Fetch links for each step
        steps.map { step ->
            val links = StepLinks.selectAll()
                .where { StepLinks.stepId eq step.id }
                .map { toStepLinkDto(it) }
            step.copy(links = links)
        }
    }

    suspend fun getFavorites(userId: Long): List<RoadmapDto> = dbQuery {
        (Favorites innerJoin Roadmaps)
            .select(Roadmaps.columns)
            .where { Favorites.userId eq userId }
            .map { toRoadmapDto(it) }
    }

    suspend fun toggleFavorite(userId: Long, roadmapId: Long): Boolean = dbQuery {
        val existing = Favorites.selectAll()
            .where { (Favorites.userId eq userId) and (Favorites.roadmapId eq roadmapId) }
            .singleOrNull()

        if (existing != null) {
            Favorites.deleteWhere { (Favorites.userId eq userId) and (Favorites.roadmapId eq roadmapId) }
            false
        } else {
            Favorites.insert {
                it[Favorites.userId] = userId
                it[Favorites.roadmapId] = roadmapId
                it[Favorites.createdAt] = LocalDateTime.now()
            }
            true
        }
    }

    suspend fun getUserProgress(userId: Long, roadmapId: Long): UserRoadmapProgressDto? = dbQuery {
        UserRoadmapProgress.selectAll()
            .where { (UserRoadmapProgress.userId eq userId) and (UserRoadmapProgress.roadmapId eq roadmapId) }
            .singleOrNull()?.let {
                UserRoadmapProgressDto(
                    userId,
                    roadmapId,
                    it[UserRoadmapProgress.progressPercent],
                    it[UserRoadmapProgress.lastStepId]?.value,
                    it[UserRoadmapProgress.updatedAt].toString(),
                    it[UserRoadmapProgress.inProgress]
                )
            }
    }

    suspend fun getAllUserProgress(userId: Long): List<UserRoadmapProgressDto> = dbQuery {
        UserRoadmapProgress.selectAll()
            .where { UserRoadmapProgress.userId eq userId }
            .orderBy(UserRoadmapProgress.updatedAt to SortOrder.DESC)
            .map {
                UserRoadmapProgressDto(
                    userId,
                    it[UserRoadmapProgress.roadmapId].value,
                    it[UserRoadmapProgress.progressPercent],
                    it[UserRoadmapProgress.lastStepId]?.value,
                    it[UserRoadmapProgress.updatedAt].toString(),
                    it[UserRoadmapProgress.inProgress]
                )
            }
    }
    
    suspend fun startRoadmap(userId: Long, roadmapId: Long) = dbQuery {
        // 1. Reset inProgress for all other roadmaps of this user
        UserRoadmapProgress.update({ UserRoadmapProgress.userId eq userId }) {
            it[inProgress] = false
        }

        // 2. Set inProgress = true for the specific roadmap
        val existing = UserRoadmapProgress.selectAll()
            .where { (UserRoadmapProgress.userId eq userId) and (UserRoadmapProgress.roadmapId eq roadmapId) }
            .singleOrNull()

        if (existing != null) {
            UserRoadmapProgress.update({ (UserRoadmapProgress.userId eq userId) and (UserRoadmapProgress.roadmapId eq roadmapId) }) {
                it[inProgress] = true
                it[updatedAt] = LocalDateTime.now()
            }
        } else {
            UserRoadmapProgress.insert {
                it[UserRoadmapProgress.userId] = userId
                it[UserRoadmapProgress.roadmapId] = roadmapId
                it[progressPercent] = 0
                it[inProgress] = true
                it[updatedAt] = LocalDateTime.now()
            }
        }
    }
    
    suspend fun markStepCompleted(userId: Long, stepId: Long, isCompleted: Boolean) = dbQuery {
         // 1. Update Step Progress
         val existing = UserStepProgress.selectAll()
             .where { (UserStepProgress.userId eq userId) and (UserStepProgress.stepId eq stepId) }
             .singleOrNull()

         if (existing != null) {
             UserStepProgress.update({ (UserStepProgress.userId eq userId) and (UserStepProgress.stepId eq stepId) }) {
                 it[UserStepProgress.isCompleted] = isCompleted
                 it[UserStepProgress.completedAt] = if (isCompleted) LocalDateTime.now() else null
             }
         } else {
             UserStepProgress.insert {
                 it[UserStepProgress.userId] = userId
                 it[UserStepProgress.stepId] = stepId
                 it[UserStepProgress.isCompleted] = isCompleted
                 it[UserStepProgress.completedAt] = if (isCompleted) LocalDateTime.now() else null
             }
         }

         // 2. Update Roadmap Progress (Calculate percentage)
         val roadmapId = RoadmapSteps.select(RoadmapSteps.roadmapId)
             .where { RoadmapSteps.id eq stepId }
             .singleOrNull()?.get(RoadmapSteps.roadmapId)?.value

         if (roadmapId != null) {
             updateRoadmapProgress(userId, roadmapId, stepId)
         }
    }

    private fun updateRoadmapProgress(userId: Long, roadmapId: Long, lastStepId: Long) {
        val totalSteps = RoadmapSteps.selectAll().where { RoadmapSteps.roadmapId eq roadmapId }.count()
        if (totalSteps == 0L) return

        val completedSteps = (UserStepProgress innerJoin RoadmapSteps)
            .select(UserStepProgress.id)
            .where { 
                (UserStepProgress.userId eq userId) and 
                (RoadmapSteps.roadmapId eq roadmapId) and 
                (UserStepProgress.isCompleted eq true) 
            }.count()

        val percent = ((completedSteps.toDouble() / totalSteps.toDouble()) * 100).toInt()

        val existing = UserRoadmapProgress.selectAll()
            .where { (UserRoadmapProgress.userId eq userId) and (UserRoadmapProgress.roadmapId eq roadmapId) }
            .singleOrNull()

        if (existing != null) {
            UserRoadmapProgress.update({ (UserRoadmapProgress.userId eq userId) and (UserRoadmapProgress.roadmapId eq roadmapId) }) {
                it[UserRoadmapProgress.progressPercent] = percent
                it[UserRoadmapProgress.lastStepId] = lastStepId
                it[UserRoadmapProgress.updatedAt] = LocalDateTime.now()
            }
        } else {
            UserRoadmapProgress.insert {
                it[UserRoadmapProgress.userId] = userId
                it[UserRoadmapProgress.roadmapId] = roadmapId
                it[UserRoadmapProgress.progressPercent] = percent
                it[UserRoadmapProgress.lastStepId] = lastStepId
                it[UserRoadmapProgress.updatedAt] = LocalDateTime.now()
            }
        }
    }

    private fun toRoadmapDto(row: ResultRow): RoadmapDto = RoadmapDto(
        row[Roadmaps.id].value,
        row[Roadmaps.title],
        row[Roadmaps.description],
        row[Roadmaps.imageUrl],
        row[Roadmaps.createdAt].toString()
    )

    private fun toRoadmapStepDto(row: ResultRow): RoadmapStepDto = RoadmapStepDto(
        row[RoadmapSteps.id].value,
        row[RoadmapSteps.roadmapId].value,
        row[RoadmapSteps.title],
        row[RoadmapSteps.description],
        row[RoadmapSteps.imageUrl],
        row[RoadmapSteps.orderIndex]
    )

    private fun toStepLinkDto(row: ResultRow): StepLinkDto = StepLinkDto(
        row[StepLinks.id].value,
        row[StepLinks.stepId].value,
        row[StepLinks.title],
        row[StepLinks.url]
    )
}