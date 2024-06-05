package com.hyfish.app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.LoginRequest
import com.hyfish.app.data.api.RegisterRequest
import com.hyfish.app.data.pref.UserModel
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {
    private val apiService = ApiConfig.getApiService()

    private val _userModel = MutableLiveData<UserModel>()
    val userModel: LiveData<UserModel> = _userModel

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(data: RegisterRequest) = apiService.register(data)

    suspend fun login(data: LoginRequest) = apiService.login(data)

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}
