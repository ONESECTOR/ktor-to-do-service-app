package com.example.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

data class CurrentUser(
    val userId: Int,
    val username: String
)

fun ApplicationCall.getCurrentUser(): CurrentUser? {
    val principal = authentication.principal<JWTPrincipal>()
    return principal?.let {
        val userId = it.payload.getClaim("userId").asInt()
        val username = it.payload.getClaim("username").asString()
        if (userId != null && username != null) {
            CurrentUser(userId, username)
        } else {
            null
        }
    }
} 