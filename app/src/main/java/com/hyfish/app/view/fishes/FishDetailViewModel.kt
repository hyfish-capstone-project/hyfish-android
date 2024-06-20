package com.hyfish.app.view.fishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.FishRepository
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.api.FishDetailItem
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FishDetailViewModel(private val fishRepo: FishRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _fishDetail = MutableLiveData<FishDetailItem>()
    val fishDetail: LiveData<FishDetailItem> = _fishDetail

    fun getFish(id: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = fishRepo.getFish(id)
                _fishDetail.postValue(result.data)
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