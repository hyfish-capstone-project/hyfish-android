package com.hyfish.app.data

import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.CaptureResponse
import com.hyfish.app.data.api.PostCaptureResponse
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ScanRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun uploadScan(
        type: RequestBody, image: MultipartBody.Part
    ): PostCaptureResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.createCapture(type, image)
    }

    suspend fun getCaptures(): CaptureResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getCaptures()
    }

    companion object {
        @Volatile
        private var instance: ScanRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): ScanRepository = instance ?: synchronized(this) {
            instance ?: ScanRepository(userPreference)
        }.also { instance = it }
    }
}
