package com.hyfish.app.data

import com.hyfish.app.data.api.ApiConfig
import com.hyfish.app.data.api.ArticleResponse
import com.hyfish.app.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ArticleRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun getArticles(): ArticleResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getArticles()
    }

    suspend fun getArticle(id: String): ArticleResponse {
        val user = runBlocking { userPreference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return apiService.getArticle(id)
    }

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): ArticleRepository =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository(userPreference)
            }.also { instance = it }
    }
}
