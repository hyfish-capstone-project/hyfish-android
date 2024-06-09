package com.hyfish.app.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.ArticleRepository
import com.hyfish.app.data.UserRepository
import com.hyfish.app.data.api.ArticleItem
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val userRepo: UserRepository,
    private val articleRepo: ArticleRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _articles = MutableLiveData<List<ArticleItem>>()
    val articles: LiveData<List<ArticleItem>> = _articles

    fun getSession(): LiveData<UserModel> {
        return userRepo.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }

    fun getArticles() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = articleRepo.getArticles()
                _articles.postValue(result.data)
                _loading.postValue(false)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
//                _message.postValue(Event(errorMessage ?: "An error occurred"))
                _loading.postValue(false)
            } catch (e: Exception) {
                _loading.postValue(false)
            }
        }
    }
}
