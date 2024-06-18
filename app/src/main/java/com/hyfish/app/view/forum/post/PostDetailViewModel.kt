package com.hyfish.app.view.forum.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.ForumRepository
import com.hyfish.app.data.api.CommentsItem
import com.hyfish.app.data.api.CreateCommentRequest
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.util.EventOnce
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PostDetailViewModel(
    private val forumRepo: ForumRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _newComment = MutableLiveData<EventOnce<CommentsItem>>()
    val newComment: LiveData<EventOnce<CommentsItem>> = _newComment

    private val _newLike = MutableLiveData<EventOnce<Boolean>>()
    val newLike: LiveData<EventOnce<Boolean>> = _newLike

    fun createComment(postId: Int, message: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val data = CreateCommentRequest(message)
                val result = forumRepo.createComment(postId, data)
                _newComment.postValue(EventOnce(result.data))
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

    fun likePost(postId: Int) {
        viewModelScope.launch {
            try {
                forumRepo.likePost(postId)
                _newLike.postValue(EventOnce(true))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
            }
        }
    }

    fun unlikePost(postId: Int) {
        viewModelScope.launch {
            try {
                forumRepo.unlikePost(postId)
                _newLike.postValue(EventOnce(false))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
            }
        }
    }
}