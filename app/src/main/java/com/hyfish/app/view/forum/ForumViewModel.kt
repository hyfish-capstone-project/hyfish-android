package com.hyfish.app.view.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.ForumRepository
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.api.PostItem
import com.hyfish.app.util.EventOnce
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ForumViewModel(
    private val forumRepo: ForumRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _forums = MutableLiveData<List<PostItem>>()
    val forums: LiveData<List<PostItem>> = _forums

    private val _message = MutableLiveData<EventOnce<String>>()
    val message: LiveData<EventOnce<String>> = _message

    fun getForums(query: String? = null) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = if (query.isNullOrEmpty()) {
                    forumRepo.getForums()
                } else {
                    forumRepo.searchForums(query)
                }
                _forums.postValue(result.data.sortedByDescending { it.createdAt })
                _loading.postValue(false)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _message.postValue(EventOnce(errorMessage))
                _loading.postValue(false)
            } catch (e: Exception) {
                _message.postValue(EventOnce(e.message ?: "Unknown error"))
                _loading.postValue(false)
            }
        }
    }
}