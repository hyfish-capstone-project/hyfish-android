package com.hyfish.app.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hyfish.app.data.ArticleRepository
import com.hyfish.app.data.FishRepository
import com.hyfish.app.data.ForumRepository
import com.hyfish.app.data.ScanRepository
import com.hyfish.app.data.UserRepository
import com.hyfish.app.di.Injection
import com.hyfish.app.view.fishes.FishDetailViewModel
import com.hyfish.app.view.fishes.FishesViewModel
import com.hyfish.app.view.forum.ForumViewModel
import com.hyfish.app.view.forum.post.PostAddViewModel
import com.hyfish.app.view.forum.post.PostDetailViewModel
import com.hyfish.app.view.history.HistoryViewModel
import com.hyfish.app.view.home.HomeViewModel
import com.hyfish.app.view.login.LoginViewModel
import com.hyfish.app.view.register.RegisterViewModel
import com.hyfish.app.view.scan.ScanViewModel

class ViewModelFactory(
    private val userRepo: UserRepository,
    private val articleRepo: ArticleRepository,
    private val forumRepo: ForumRepository,
    private val scanRepo: ScanRepository,
    private val fishRepo: FishRepository,
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

            modelClass.isAssignableFrom(ForumViewModel::class.java) -> {
                ForumViewModel(forumRepo) as T
            }

            modelClass.isAssignableFrom(PostAddViewModel::class.java) -> {
                PostAddViewModel(forumRepo) as T
            }

            modelClass.isAssignableFrom(PostDetailViewModel::class.java) -> {
                PostDetailViewModel(forumRepo) as T
            }

            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(scanRepo, fishRepo) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(scanRepo, fishRepo) as T
            }

            modelClass.isAssignableFrom(FishesViewModel::class.java) -> {
                FishesViewModel(fishRepo) as T
            }

            modelClass.isAssignableFrom(FishDetailViewModel::class.java) -> {
                FishDetailViewModel(fishRepo) as T
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
                        Injection.provideForumRepository(context),
                        Injection.provideScanRepository(context),
                        Injection.provideFishRepository(context),
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
