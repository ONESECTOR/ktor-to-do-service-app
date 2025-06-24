package com.example.services

import com.example.database.tables.Tasks
import com.example.models.Task
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TaskService {

    fun getAll(userId: Int): List<Task> = transaction {
        Tasks.selectAll().where { Tasks.userId eq userId }.map {
            Task(
                id = it[Tasks.id],
                title = it[Tasks.title],
                description = it[Tasks.description],
                priority = it[Tasks.priority]
            )
        }
    }

    fun getById(taskId: Int, userId: Int): Task? = transaction {
        Tasks.selectAll().where { (Tasks.id eq taskId) and (Tasks.userId eq userId) }.mapNotNull {
            Task(
                id = it[Tasks.id],
                title = it[Tasks.title],
                description = it[Tasks.description],
                priority = it[Tasks.priority]
            )
        }.singleOrNull()
    }

    fun create(task: Task, userId: Int): Task = transaction {
        val insertedId = Tasks.insert {
            it[title] = task.title
            it[description] = task.description
            it[priority] = task.priority
            it[Tasks.userId] = userId
        } get Tasks.id

        task.copy(id = insertedId)
    }

    fun update(taskId: Int, task: Task, userId: Int): Boolean = transaction {
        Tasks.update({ (Tasks.id eq taskId) and (Tasks.userId eq userId) }) {
            it[title] = task.title
            it[description] = task.description
            it[priority] = task.priority
        } > 0
    }

    fun delete(taskId: Int, userId: Int): Boolean = transaction {
        Tasks.deleteWhere { (Tasks.id eq taskId) and (Tasks.userId eq userId) } > 0
    }
}