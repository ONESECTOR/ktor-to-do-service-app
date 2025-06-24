package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

object JwtUtil {
    
    private const val SECRET = "your-secret-key" // В продакшене нужно читать из переменных окружения
    private const val ISSUER = "ktor-todo-app"
    private const val AUDIENCE = "ktor-todo-users"
    private const val EXPIRATION_TIME = 3600000 // 1 час в миллисекундах
    
    private val algorithm = Algorithm.HMAC256(SECRET)
    
    fun generateToken(userId: Int, username: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)
    }
    
    fun verifyToken(token: String): TokenPayload? {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .build()
            
            val decodedJWT = verifier.verify(token)
            
            TokenPayload(
                userId = decodedJWT.getClaim("userId").asInt(),
                username = decodedJWT.getClaim("username").asString()
            )
        } catch (exception: JWTVerificationException) {
            null
        }
    }
}

data class TokenPayload(
    val userId: Int,
    val username: String
) 