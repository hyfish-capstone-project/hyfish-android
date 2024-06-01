package com.hyfish.app.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.UserRepository
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.api.RegisterData
import com.hyfish.app.data.api.RegisterRequest
import com.hyfish.app.util.EventOnce
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<EventOnce<String>>()
    val error: LiveData<EventOnce<String>> = _error

    private val _user = MutableLiveData<RegisterData>()
    val user: LiveData<RegisterData> = _user

    fun register(data: RegisterRequest) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(data)
                _loading.postValue(false)
                _user.postValue(result.data)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _loading.postValue(false)
                _error.postValue(EventOnce(errorMessage ?: "An error occurred"))
            }
        }

    }
}
