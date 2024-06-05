package com.hyfish.app.data.api

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String,
)