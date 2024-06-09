package com.hyfish.app.data.api

import com.hyfish.app.data.api.auth.LoginRequest
import com.hyfish.app.data.api.auth.LoginResponse
import com.hyfish.app.data.api.auth.LogoutResponse
import com.hyfish.app.data.api.auth.RegisterRequest
import com.hyfish.app.data.api.auth.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("users/register")
    suspend fun register(@Body data: RegisterRequest): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body data: LoginRequest): LoginResponse

    @POST("users/logout")
    suspend fun logout(): LogoutResponse

    @GET("articles")
    suspend fun getArticles(): ArticleResponse

    @GET("articles/{id}")
    suspend fun getArticle(@Path("id") id: String): ArticleResponse

}
