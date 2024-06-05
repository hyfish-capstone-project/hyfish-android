package com.hyfish.app.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/register")
    suspend fun register(@Body data: RegisterRequest): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body data: LoginRequest): LoginResponse
}
