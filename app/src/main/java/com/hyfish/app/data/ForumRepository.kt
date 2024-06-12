package com.hyfish.app.data

import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.CreatePostResponse
import com.hyfish.app.data.api.ForumResponse
import com.hyfish.app.data.api.TagsItem
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ForumRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun getForums(): ForumResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getForums()
    }

    suspend fun createPost(
        title: RequestBody,
        body: RequestBody,
        tags: List<TagsItem>,
        images: List<MultipartBody.Part>
    ): CreatePostResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.createPost(title, body, tags, images)
    }

    companion object {
        @Volatile
        private var instance: ForumRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): ForumRepository =
            instance ?: synchronized(this) {
                instance ?: ForumRepository(userPreference)
            }.also { instance = it }
    }
}
