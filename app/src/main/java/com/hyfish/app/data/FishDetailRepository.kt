package com.hyfish.app.data

import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.FishDetailResponse
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class FishDetailRepository private constructor(private val userPreference: UserPreference) {
    private var cachedDetail: FishDetailResponse? = null

    suspend fun getDetailFish(): FishDetailResponse {
        cachedDetail?.let {
            return it
        }

        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getDetailFishes().also {
            cachedDetail = it
        }
    }

    companion object {
        @Volatile
        private var instance: FishDetailRepository? = null
        fun getInstance(userPreference: UserPreference): FishDetailRepository =
            instance ?: synchronized(this) {
                instance ?: FishDetailRepository(userPreference)
            }.also {
                instance = it
            }
    }
}