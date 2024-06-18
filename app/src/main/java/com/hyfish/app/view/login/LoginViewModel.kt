package com.hyfish.app.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.UserRepository
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.api.auth.LoginData
import com.hyfish.app.data.api.auth.LoginRequest
import com.hyfish.app.data.pref.UserModel
import com.hyfish.app.util.EventOnce
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<EventOnce<String>>()
    val error: LiveData<EventOnce<String>> = _error

    private val _user = MutableLiveData<LoginData>()
    val user: LiveData<LoginData> = _user

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(data: LoginRequest) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(data)
                _loading.postValue(false)
                _user.postValue(response.data)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _loading.postValue(false)
                _error.postValue(EventOnce(errorMessage))
            } catch (e: Throwable) {
                _loading.postValue(false)
                _error.postValue(EventOnce(e.message ?: "Something went wrong"))
            }
        }
    }
}