package com.example.routes

import com.example.controllers.TaskController
import io.ktor.server.routing.*

fun Route.taskRoutes(taskController: TaskController) {
    route(path = "/tasks") {
        get { taskController.getAll(call) }
        get(path = "/{id}") { taskController.getById(call) }
        post { taskController.create(call) }
        put(path = "/{id}") { taskController.update(call) }
        delete(path = "/{id}") { taskController.delete(call) }
    }
}

