package com.example.controllers

import com.example.models.User
import com.example.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class AuthController(private val authService: AuthService) {

    suspend fun register(call: ApplicationCall) {
        val user = call.receive<User>()
        val response = authService.register(user)

        call.respond(
            status = HttpStatusCode.Created,
            message = response
        )
    }

    suspend fun login(call: ApplicationCall) {
        val user = call.receive<User>()
        val token = authService.login(user)

        call.respond(mapOf("token" to token))
    }
}