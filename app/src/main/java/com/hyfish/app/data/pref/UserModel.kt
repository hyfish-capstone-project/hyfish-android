package com.hyfish.app.data.pref

data class UserModel(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)
