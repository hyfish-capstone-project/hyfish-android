package com.hyfish.app.data

import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.auth.LoginRequest
import com.hyfish.app.data.api.auth.RegisterRequest
import com.hyfish.app.data.pref.UserModel
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {
    private val apiServiceNoToken = ApiConfig.getApiService()

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(data: RegisterRequest) = apiServiceNoToken.register(data)

    suspend fun login(data: LoginRequest) = apiServiceNoToken.login(data)

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference)
        }.also { instance = it }
    }
}
