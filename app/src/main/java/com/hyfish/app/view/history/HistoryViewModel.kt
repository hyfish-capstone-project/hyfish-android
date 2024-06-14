package com.hyfish.app.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.ScanRepository
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.data.api.ErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HistoryViewModel(
    private val scanRepo: ScanRepository,
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _captures = MutableLiveData<List<CaptureItem>>()
    val captures: LiveData<List<CaptureItem>> = _captures

    fun getCaptures() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = scanRepo.getCaptures()
                val sorted = result.data
                    .sortedByDescending { it.createdAt }
                _captures.postValue(sorted)
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