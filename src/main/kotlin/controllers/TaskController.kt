package com.example.controllers

import com.example.models.Task
import com.example.services.TaskService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class TaskController {

    private val taskService = TaskService()

    suspend fun getAll(call: ApplicationCall) {
        val tasks = taskService.getAll()
        call.respond(tasks)
    }

    suspend fun getById(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
            return
        }
        
        val task = taskService.getById(id)
        if (task != null) {
            call.respond(task)
        } else {
            call.respond(HttpStatusCode.NotFound, "Task not found")
        }
    }

    suspend fun create(call: ApplicationCall) {
        try {
            val inputTask = call.receive<Task>()
            val createdTask = taskService.create(inputTask)
            call.respond(HttpStatusCode.Created, createdTask)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid task data")
        }
    }

    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
            return
        }
        
        try {
            val updatedTask = call.receive<Task>()
            val success = taskService.update(id, updatedTask)
            
            if (success) {
                val task = taskService.getById(id)
                call.respond(task!!)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid task data")
        }
    }

    suspend fun delete(call: ApplicationCall) {
        val id = call.parameters["id"]?.toIntOrNull()
        
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid task ID")
            return
        }
        
        val success = taskService.delete(id)
        if (success) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound, "Task not found")
        }
    }
}