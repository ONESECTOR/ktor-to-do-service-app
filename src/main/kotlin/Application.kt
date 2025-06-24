package com.example

import com.example.controllers.AuthController
import com.example.controllers.TaskController
import com.example.database.factory.DatabaseFactory
import com.example.routes.authRoutes
import com.example.routes.taskRoutes
import com.example.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
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
    install(plugin = StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError, cause.localizedMessage
            )
        }
    }
    routing {
        authRoutes(authController)
        taskRoutes(taskController)
    }
}
