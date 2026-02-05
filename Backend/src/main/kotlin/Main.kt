package ru.vsu

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import ru.vsu.config.AppConfig
import ru.vsu.config.DatabaseFactory
import ru.vsu.routes.authRoutes
import ru.vsu.routes.roadmapRoutes
import ru.vsu.routes.userRoutes
import ru.vsu.services.RoadmapService
import ru.vsu.services.StorageService
import ru.vsu.services.UserService
import ru.vsu.util.TokenService

fun main() {
    val config = AppConfig()
    DatabaseFactory.init(config)
    ru.vsu.config.DatabaseSeeder.seed()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = { module(config) })
        .start(wait = true)
}

fun Application.module(config: AppConfig) {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = config.jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(config.jwtSecret))
                    .withAudience(config.jwtAudience)
                    .withIssuer(config.jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    // Initialize Services
    val userService = UserService()
    val tokenService = TokenService(config)
    val storageService = StorageService(config)
    val roadmapService = RoadmapService()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        authRoutes(userService, tokenService)
        userRoutes(userService, storageService)
        roadmapRoutes(roadmapService)
    }
}
