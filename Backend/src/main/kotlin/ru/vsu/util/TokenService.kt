package ru.vsu.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ru.vsu.config.AppConfig
import ru.vsu.models.UserDto
import java.util.Date

class TokenService(private val config: AppConfig) {
    fun generateToken(user: UserDto): String {
        return JWT.create()
            .withAudience(config.jwtAudience)
            .withIssuer(config.jwtIssuer)
            .withClaim("id", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + 604800000)) // 7 days
            .sign(Algorithm.HMAC256(config.jwtSecret))
    }
}
