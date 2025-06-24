package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.controllers.AuthController
import com.example.controllers.TaskController
import com.example.database.factory.DatabaseFactory
import com.example.services.AuthService
import com.example.utils.configureAuthRoutes
import com.example.utils.configureTaskRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing

private val authService = AuthService()
private val authController = AuthController(authService)
private val taskController = TaskController()

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    install(plugin = CallLogging)
    install(plugin = CORS) {
        anyHost()
    }
    install(plugin = ContentNegotiation) {
        json()
    }
    
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256("your-secret-key"))
                    .withIssuer("ktor-todo-app")
                    .withAudience("ktor-todo-users")
                    .build()
            )
            validate { credential ->
                // Проверяем что токен корректно расшифровался
                if (credential.payload.getClaim("userId").asInt() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Токен недействителен или отсутствует")
            }
        }
    }
    
    install(plugin = StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError, cause.localizedMessage
            )
        }
    }
    
    routing {
        configureAuthRoutes(authController)
        configureTaskRoutes(taskController)
    }
}
