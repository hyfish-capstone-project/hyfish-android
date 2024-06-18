package com.hyfish.app.data

import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.FishResponse
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class FishRepository private constructor(
    private val userPreference: UserPreference
) {
    private var cachedFishes: FishResponse? = null

    suspend fun getFishes(): FishResponse {
        cachedFishes?.let { return it }

        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getFishes().also {
            cachedFishes = it
        }
    }

    companion object {
        @Volatile
        private var instance: FishRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): FishRepository =
            instance ?: synchronized(this) {
                instance ?: FishRepository(userPreference)
            }.also { instance = it }
    }
}
