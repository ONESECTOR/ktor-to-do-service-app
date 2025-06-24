package com.example.services

import com.example.models.User

class AuthService {

    fun register(user: User): Map<String, String> {
        println("Registering user: ${user.username}")
        return mapOf("message" to "User registered successfully")
    }

    fun login(user: User): String {
        println("Logging in user: ${user.username}")
        return "mock-jwt-token"
    }
}