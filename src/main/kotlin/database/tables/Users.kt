package com.example.database.tables

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer(name = "id").autoIncrement()
    val username = varchar(name = "username", length = 64)
    val password = varchar(name = "password", length = 64)

    override val primaryKey = PrimaryKey(firstColumn = id)
}