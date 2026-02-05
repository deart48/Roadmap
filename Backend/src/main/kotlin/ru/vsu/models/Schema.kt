package ru.vsu.models

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Users : LongIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = text("password_hash")
    val createdAt = datetime("created_at")
}

object UserProfiles : LongIdTable("user_profiles") {
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val name = varchar("name", 255).nullable()
    val surname = varchar("surname", 255).nullable()
    val birthDate = date("date").nullable()
    val about = text("about").nullable()
    val avatarUrl = text("avatar_url").nullable()
}

object Roadmaps : LongIdTable("roadmaps") {
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val imageUrl = text("image_url").nullable()
    val createdAt = datetime("created_at")
}

object RoadmapSteps : LongIdTable("roadmap_steps") {
    val roadmapId = reference("roadmap_id", Roadmaps, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val imageUrl = text("image_url").nullable()
    val orderIndex = integer("order_index")
}

object StepLinks : LongIdTable("step_links") {
    val stepId = reference("step_id", RoadmapSteps, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 255).nullable()
    val url = text("url")
}

object Favorites : LongIdTable("favorites") {
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
    val roadmapId = reference("roadmap_id", Roadmaps, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at")

    init {
        uniqueIndex("uq_favorite", userId, roadmapId)
    }
}

object UserRoadmapProgress : LongIdTable("user_roadmap_progress") {
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
    val roadmapId = reference("roadmap_id", Roadmaps, onDelete = ReferenceOption.CASCADE)
    val progressPercent = integer("progress_percent").check { it.between(0, 100) }
    val lastStepId = reference("last_step_id", RoadmapSteps, onDelete = ReferenceOption.SET_NULL).nullable()
    val updatedAt = datetime("updated_at")
    val inProgress = bool("in_progress").default(false)

    init {
        uniqueIndex("uq_user_roadmap", userId, roadmapId)
    }
}

object UserStepProgress : LongIdTable("user_step_progress") {
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
    val stepId = reference("step_id", RoadmapSteps, onDelete = ReferenceOption.CASCADE)
    val isCompleted = bool("is_completed").default(false)
    val completedAt = datetime("completed_at").nullable()

    init {
        uniqueIndex("uq_user_step", userId, stepId)
    }
}
