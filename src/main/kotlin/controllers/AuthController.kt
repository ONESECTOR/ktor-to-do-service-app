package com.example.controllers

import com.example.models.*
import com.example.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class AuthController(private val authService: AuthService) {

    suspend fun register(call: ApplicationCall) {
        try {
            val request = call.receive<RegisterRequest>()

            if (request.username.isBlank() || request.password.length < 6) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        success = false,
                        message = "Имя пользователя не может быть пустым, пароль должен быть не менее 6 символов"
                    )
                )
                return
            }
            
            val result = authService.register(
                username = request.username,
                password = request.password
            )
            
            val response = AuthResponse(
                success = result.success,
                message = result.message,
                token = result.token,
                user = result.user?.let { UserResponse(id = it.id, it.username) }
            )
            
            val status = if (result.success) HttpStatusCode.Created else HttpStatusCode.BadRequest
            call.respond(status, response)
            
        } catch (_: Exception) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = AuthResponse(false, "Неверный формат данных")
            )
        }
    }

    suspend fun login(call: ApplicationCall) {
        try {
            val request = call.receive<LoginRequest>()
            
            if (request.username.isBlank() || request.password.isBlank()) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        success = false,
                        message = "Имя пользователя и пароль обязательны"
                    )
                )
                return
            }
            
            val result = authService.login(
                username = request.username,
                password = request.password
            )
            
            val response = AuthResponse(
                success = result.success,
                message = result.message,
                token = result.token,
                user = result.user?.let { UserResponse(it.id, it.username) }
            )
            
            val status = if (result.success) HttpStatusCode.OK else HttpStatusCode.Unauthorized
            call.respond(status, response)
            
        } catch (_: Exception) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = AuthResponse(
                    success = false,
                    message = "Неверный формат данных"
                )
            )
        }
    }
}