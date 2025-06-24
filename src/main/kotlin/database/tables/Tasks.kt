package com.example.database.tables

import org.jetbrains.exposed.sql.Table

object Tasks : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar(name = "title", length = 100)
    val description = text(name = "description")
    val priority = integer(name = "priority")
    val userId = integer("user_id").references(Users.id)

    override val primaryKey = PrimaryKey(firstColumn = id)
}