package com.example.database.factory

import com.example.database.tables.Tasks
import com.example.database.tables.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:h2:file:./database/todoapp;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver",
            user = "root",
            password = "root"
        )

        transaction {
            SchemaUtils.create(Users, Tasks)
        }
    }
}