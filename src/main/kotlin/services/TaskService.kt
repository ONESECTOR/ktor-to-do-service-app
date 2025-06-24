package com.example.services

import com.example.database.tables.Tasks
import com.example.models.Task
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TaskService {

    fun getAll(): List<Task> = transaction {
        Tasks.selectAll().map {
            Task(
                id = it[Tasks.id],
                title = it[Tasks.title],
                description = it[Tasks.description],
                priority = it[Tasks.priority]
            )
        }
    }

    fun getById(id: Int): Task? = transaction {
        Tasks.selectAll().where { Tasks.id eq id }.mapNotNull {
            Task(
                id = it[Tasks.id],
                title = it[Tasks.title],
                description = it[Tasks.description],
                priority = it[Tasks.priority]
            )
        }.singleOrNull()
    }

    fun create(task: Task): Task = transaction {
        val insertedId = Tasks.insert {
            it[title] = task.title
            it[description] = task.description
            it[priority] = task.priority
            it[userId] = 1 // пока жестко
        } get Tasks.id  // получение сгенерированного ID

        task.copy(id = insertedId)
    }

    fun update(id: Int, task: Task): Boolean = transaction {
        Tasks.update({ Tasks.id eq id }) {
            it[title] = task.title
            it[description] = task.description
            it[priority] = task.priority
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }
}