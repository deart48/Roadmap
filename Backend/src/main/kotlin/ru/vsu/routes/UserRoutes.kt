package ru.vsu.routes

import io.ktor.http.*
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.vsu.models.UpdateProfileRequest
import ru.vsu.services.StorageService
import ru.vsu.services.UserService

fun Route.userRoutes(userService: UserService, storageService: StorageService) {
    authenticate("auth-jwt") {
        route("/profile") {
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val profile = userService.getUserProfile(userId)
                if (profile != null) {
                    call.respond(profile)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asLong()
                val request = call.receive<UpdateProfileRequest>()
                val updatedProfile = userService.updateUserProfile(userId, request)
                if (updatedProfile != null) {
                    call.respond(updatedProfile)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
        
        // Image Upload
        post("/upload") {
             // For simplicity, assuming raw bytes or multipart. 
             // With Ktor, Multipart is common. But let's keep it simple if possible.
             // Actually, the storage service expects bytes.
             // Implementing Multipart handling here.
             
             // NOTE: Detailed multipart handling requires more code. 
             // For this prototype, I will assume the client sends the file as body or multipart.
             // Let's implement multipart.
             
             // Wait, I need 'io.ktor:ktor-server-multipart' ? It's in core usually.
             // `call.receiveMultipart()`
             
             val multipart = call.receiveMultipart()
             var fileUrl = ""
             
             multipart.forEachPart { part ->
                 if (part is io.ktor.http.content.PartData.FileItem) {
                     val fileName = part.originalFileName ?: "file"
                     val fileBytes = part.streamProvider().readBytes()
                     fileUrl = storageService.uploadFile(fileBytes, fileName, "image/jpeg") // simplified content type
                 }
                 part.dispose()
             }
             
             if (fileUrl.isNotEmpty()) {
                 call.respond(mapOf("url" to fileUrl))
             } else {
                 call.respond(HttpStatusCode.BadRequest, "No file uploaded")
             }
        }
    }
}
