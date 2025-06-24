package com.example.routes

import com.example.controllers.AuthController
import io.ktor.server.routing.*

fun Route.authRoutes(authController: AuthController) {
    route(path = "/auth") {
        post(path = "/register") { authController.register(call) }
        post(path = "/login") { authController.login(call) }
    }
}