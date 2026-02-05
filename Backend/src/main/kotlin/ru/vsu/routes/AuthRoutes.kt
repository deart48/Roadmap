package ru.vsu.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.vsu.models.AuthResponse
import ru.vsu.models.LoginRequest
import ru.vsu.models.RegisterRequest
import ru.vsu.services.UserService
import ru.vsu.util.TokenService

fun Route.authRoutes(userService: UserService, tokenService: TokenService) {
    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val user = userService.createUser(request)
            if (user != null) {
                val token = tokenService.generateToken(user)
                call.respond(AuthResponse(token, user))
            } else {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = userService.authenticate(request)
            if (user != null) {
                val token = tokenService.generateToken(user)
                call.respond(AuthResponse(token, user))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
    }
}
