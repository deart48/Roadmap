package ru.vsu.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.vsu.models.UserStepProgressDto
import ru.vsu.services.RoadmapService

fun Route.roadmapRoutes(roadmapService: RoadmapService) {
    authenticate("auth-jwt") {
        route("/roadmaps") {
            get {
                val roadmaps = roadmapService.getAllRoadmaps()
                call.respond(roadmaps)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val roadmap = roadmapService.getRoadmapById(id)
                if (roadmap != null) {
                    call.respond(roadmap)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/{id}/steps") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val steps = roadmapService.getRoadmapSteps(id)
                call.respond(steps)
            }
            
            // Favorites
            get("/favorites") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val favorites = roadmapService.getFavorites(userId)
                call.respond(favorites)
            }
            
            post("/{id}/favorite") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val isFavorite = roadmapService.toggleFavorite(userId, id)
                call.respond(mapOf("isFavorite" to isFavorite))
            }
            
            // Progress
            get("/progress") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val progressList = roadmapService.getAllUserProgress(userId)
                call.respond(progressList)
            }

            post("/{id}/start") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                roadmapService.startRoadmap(userId, id)
                call.respond(HttpStatusCode.OK)
            }

            // Progress
            get("/{id}/progress") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val progress = roadmapService.getUserProgress(userId, id)
                if (progress != null) {
                    call.respond(progress)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            
            post("/steps/{stepId}/complete") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val stepId = call.parameters["stepId"]?.toLongOrNull()
                val status = call.receive<Map<String, Boolean>>() // Expected { "isCompleted": true }
                val isCompleted = status["isCompleted"] ?: true
                
                if (stepId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                
                roadmapService.markStepCompleted(userId, stepId, isCompleted)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
