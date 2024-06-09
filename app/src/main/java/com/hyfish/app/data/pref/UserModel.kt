package com.hyfish.app.data.pref

data class UserModel(
    val token: String,
    val role: String,
    val isLogin: Boolean = true,
    val id: Int? = null,
    val username: String? = null,
    val email: String? = null,
)
