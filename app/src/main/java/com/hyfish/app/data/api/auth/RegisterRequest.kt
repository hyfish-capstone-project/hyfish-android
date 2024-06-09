package com.hyfish.app.data.api.auth

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String,
)