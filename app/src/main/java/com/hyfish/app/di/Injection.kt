package com.hyfish.app.di

import android.content.Context
import com.hyfish.app.data.ArticleRepository
import com.hyfish.app.data.FishDetailRepository
import com.hyfish.app.data.FishRepository
import com.hyfish.app.data.ForumRepository
import com.hyfish.app.data.ScanRepository
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

    fun provideForumRepository(context: Context): ForumRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return ForumRepository.getInstance(pref)
    }

    fun provideScanRepository(context: Context): ScanRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return ScanRepository.getInstance(pref)
    }

    fun provideFishRepository(context: Context): FishRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return FishRepository.getInstance(pref)
    }

    fun provideDetailFishRepository(context: Context): FishDetailRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return FishDetailRepository.getInstance(pref)
    }
}

