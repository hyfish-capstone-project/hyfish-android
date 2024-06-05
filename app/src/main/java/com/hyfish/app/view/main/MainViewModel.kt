package com.hyfish.app.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hyfish.app.data.UserRepository
import com.hyfish.app.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepo: UserRepository
) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return userRepo.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }
}
