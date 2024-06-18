package com.hyfish.app.view.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hyfish.app.data.FishRepository
import com.hyfish.app.data.ScanRepository
import com.hyfish.app.data.api.CaptureItem
import com.hyfish.app.data.api.ErrorResponse
import com.hyfish.app.data.api.FishItem
import com.hyfish.app.util.EventOnce
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class ScanViewModel(
    private val scanRepo: ScanRepository,
    private val fishRepo: FishRepository,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<EventOnce<String>>()
    val error: LiveData<EventOnce<String>> = _error

    private val _captureItem = MutableLiveData<CaptureItem>()
    val captureItem: LiveData<CaptureItem> = _captureItem

    private val _fishes = MutableLiveData<List<FishItem>>()
    val fishes: LiveData<List<FishItem>> = _fishes



    fun getFishes() {
        viewModelScope.launch {
            try {
                val result = fishRepo.getFishes()
                _fishes.postValue(result.data)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _error.postValue(EventOnce(errorMessage ?: "Something went wrong"))
            } catch (e: Exception) {
                _error.postValue(EventOnce(e.message ?: "Something went wrong"))
            }
        }
    }

    fun uploadScan(type: String, image: File) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    image.name,
                    requestImageFile
                )

                val rbType = type.toRequestBody("text/plain".toMediaType())

                val result = scanRepo.uploadScan(rbType, imagePart)
                _captureItem.postValue(result.data)
                _loading.postValue(false)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _error.postValue(EventOnce(errorMessage ?: "Something went wrong"))
                _loading.postValue(false)
            } catch (e: Exception) {
                _error.postValue(EventOnce(e.message ?: "Something went wrong"))
                _loading.postValue(false)
            }
        }
    }
}