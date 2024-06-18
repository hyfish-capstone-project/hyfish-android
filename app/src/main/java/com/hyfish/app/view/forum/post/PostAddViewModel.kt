package com.hyfish.app.view.forum.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.ForumRepository
import com.hyfish.app.data.api.ErrorResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class PostAddViewModel(
    private val forumRepo: ForumRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    fun createPost(
        title: String,
        body: String,
        tags: List<String>,
        images: List<File>
    ) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val imagesPart = images.map {
                    val requestImageFile = it.asRequestBody("image/jpeg".toMediaType())
                    MultipartBody.Part.createFormData(
                        "images[]",
                        it.name,
                        requestImageFile
                    )
                }

                val rbTitle = title.toRequestBody("text/plain".toMediaType())
                val rbBody = body.toRequestBody("text/plain".toMediaType())

                val result = forumRepo.createPost(rbTitle, rbBody, tags, imagesPart)
                _status.postValue(result.status)
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