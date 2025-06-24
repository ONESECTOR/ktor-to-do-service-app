package com.example.utils

import com.example.controllers.AuthController
import com.example.controllers.TaskController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.configureAuthRoutes(authController: AuthController) {
    route(path = "/auth") {
        post(path = "/register") { authController.register(call) }
        post(path = "/login") { authController.login(call) }
    }
}

fun Routing.configureTaskRoutes(taskController: TaskController) {
    authenticate("auth-jwt") {
        route(path = "/tasks") {
            get { taskController.getAll(call) }
            get(path = "/{id}") { taskController.getById(call) }
            post { taskController.create(call) }
            put(path = "/{id}") { taskController.update(call) }
            delete(path = "/{id}") { taskController.delete(call) }
        }
    }
} 