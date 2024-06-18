package com.hyfish.app.view.fishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.FishRepository
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.api.FishItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FishesViewModel(
    private val fishRepo: FishRepository,
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _fishes = MutableLiveData<List<FishItem>>()
    val fishes: LiveData<List<FishItem>> = _fishes

    fun getFishes() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = fishRepo.getFishes()
                _fishes.postValue(result.data)
                _loading.postValue(false)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _loading.postValue(false)
            } catch (e: Exception) {
                _loading.postValue(false)
            }
        }
    }
}