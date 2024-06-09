package com.hyfish.app.di

import android.content.Context
import com.hyfish.app.data.ArticleRepository
import com.hyfish.app.data.UserRepository
import com.hyfish.app.data.pref.UserPreference
import com.hyfish.app.data.pref.dataStore

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideArticleRepository(context: Context): ArticleRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return ArticleRepository.getInstance(pref)
    }
}

