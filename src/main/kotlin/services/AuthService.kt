package com.example.services

import com.example.database.tables.Users
import com.example.models.User
import com.example.utils.JwtUtil
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class AuthService {
    
    data class AuthResult(
        val success: Boolean,
        val message: String,
        val token: String? = null,
        val user: User? = null
    )

    fun register(username: String, password: String): AuthResult {
        return transaction {
            try {
                val existingUser = Users.selectAll().where { Users.username eq username }.singleOrNull()
                if (existingUser != null) {
                    return@transaction AuthResult(false, "Пользователь уже существует")
                }
                
                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                
                val userId = Users.insert {
                    it[Users.username] = username
                    it[Users.password] = hashedPassword
                } get Users.id
                
                val user = User(id = userId, username = username, password = "")
                val token = JwtUtil.generateToken(userId, username)
                
                AuthResult(true, "Регистрация успешна", token, user)
                
            } catch (e: Exception) {
                AuthResult(false, "Ошибка при регистрации: ${e.message}")
            }
        }
    }

    fun login(username: String, password: String): AuthResult {
        return transaction {
            try {
                val userRow = Users.selectAll().where { Users.username eq username }.singleOrNull()
                    ?: return@transaction AuthResult(false, "Неверные учетные данные")
                
                val storedPassword = userRow[Users.password]
                if (!BCrypt.checkpw(password, storedPassword)) {
                    return@transaction AuthResult(false, "Неверные учетные данные")
                }
                
                val user = User(
                    id = userRow[Users.id],
                    username = userRow[Users.username],
                    password = ""
                )
                
                val token = JwtUtil.generateToken(user.id, user.username)
                
                AuthResult(true, "Авторизация успешна", token, user)
                
            } catch (e: Exception) {
                AuthResult(false, "Ошибка при авторизации: ${e.message}")
            }
        }
    }
}
