package com.example.corpalivapp.network.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val token: String?,
    val id: Long?,
    val nombre: String?,
    val email: String?,
    val rol: String?,
    val message: String?
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val status: String,
    val message: String
)
