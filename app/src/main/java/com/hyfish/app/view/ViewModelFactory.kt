package com.hyfish.app.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyfish.app.data.ArticleRepository
import com.hyfish.app.data.UserRepository
import com.hyfish.app.di.Injection
import com.hyfish.app.view.home.HomeViewModel
import com.hyfish.app.view.login.LoginViewModel
import com.hyfish.app.view.register.RegisterViewModel

class ViewModelFactory(
    private val userRepo: UserRepository,
    private val articleRepo: ArticleRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepo, articleRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideArticleRepository(context),
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
