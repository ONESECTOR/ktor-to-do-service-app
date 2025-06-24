package com.example.controllers

import com.example.models.Task
import com.example.services.TaskService
import com.example.utils.getCurrentUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class TaskController {

    private val taskService = TaskService()

    suspend fun getAll(call: ApplicationCall) {
        val currentUser = call.getCurrentUser()
        if (currentUser == null) {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = "Пользователь не авторизован"
            )
            return
        }
        
        val tasks = taskService.getAll(userId = currentUser.userId)
        call.respond(tasks)
    }

    suspend fun getById(call: ApplicationCall) {
        val currentUser = call.getCurrentUser()
        if (currentUser == null) {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = "Пользователь не авторизован"
            )
            return
        }
        
        val id = call.parameters["id"]?.toIntOrNull()
        
        if (id == null) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invalid task ID"
            )
            return
        }
        
        val task = taskService.getById(
            taskId = id,
            userId = currentUser.userId
        )
        if (task != null) {
            call.respond(task)
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "Task not found"
            )
        }
    }

    suspend fun create(call: ApplicationCall) {
        val currentUser = call.getCurrentUser()
        if (currentUser == null) {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = "Пользователь не авторизован"
            )
            return
        }
        
        try {
            val inputTask = call.receive<Task>()
            val createdTask = taskService.create(task = inputTask, userId = currentUser.userId)
            call.respond(status = HttpStatusCode.Created, message = createdTask)
        } catch (_: Exception) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invalid task data"
            )
        }
    }

    suspend fun update(call: ApplicationCall) {
        val currentUser = call.getCurrentUser()
        if (currentUser == null) {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = "Пользователь не авторизован"
            )
            return
        }
        
        val id = call.parameters["id"]?.toIntOrNull()
        
        if (id == null) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invalid task ID"
            )
            return
        }
        
        try {
            val updatedTask = call.receive<Task>()
            val success = taskService.update(
                taskId = id,
                updatedTask,
                userId = currentUser.userId
            )
            
            if (success) {
                val task = taskService.getById(
                    taskId = id,
                    userId = currentUser.userId
                )
                if (task != null) {
                    call.respond(task)
                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "Task not found"
                    )
                }
            } else {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    message = "Task not found"
                )
            }
        } catch (_: Exception) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invalid task data"
            )
        }
    }

    suspend fun delete(call: ApplicationCall) {
        val currentUser = call.getCurrentUser()
        if (currentUser == null) {
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = "Пользователь не авторизован"
            )
            return
        }
        
        val id = call.parameters["id"]?.toIntOrNull()
        
        if (id == null) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Invalid task ID"
            )
            return
        }
        
        val success = taskService.delete(
            taskId = id,
            userId = currentUser.userId
        )
        if (success) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(
                status = HttpStatusCode.NotFound,
                message = "Task not found"
            )
        }
    }
}