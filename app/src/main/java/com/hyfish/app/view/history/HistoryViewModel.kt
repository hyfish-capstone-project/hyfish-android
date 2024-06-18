package com.hyfish.app.view.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyfish.app.data.FishRepository
import com.hyfish.app.data.ScanRepository
import com.hyfish.app.data.api.CaptureItemWithFish
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val scanRepo: ScanRepository,
    private val fishRepo: FishRepository,
) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _capturesWithFishes = MutableLiveData<List<CaptureItemWithFish>>()
    val capturesWithFishes: LiveData<List<CaptureItemWithFish>> = _capturesWithFishes

    fun getCapturesWithFishes() {
        _loading.postValue(true)
        viewModelScope.launch {
            val captures = scanRepo.getCaptures().data
            val fishes = fishRepo.getFishes().data

            val capturesWithFish = captures.sortedByDescending { it.updatedAt }.map { capture ->
                val fish = fishes.find { it.id == capture.fishId }

                return@map CaptureItemWithFish(
                    id = capture.id,
                    fishId = capture.fishId,
                    imageUrl = capture.imageUrl,
                    freshness = capture.freshness,
                    score = capture.score,
                    userId = capture.userId,
                    updatedAt = capture.updatedAt,
                    createdAt = capture.createdAt,
                    type = capture.type,
                    fish = fish
                )
            }

            Log.d("HistoryViewModel", "captures: $capturesWithFish")

            _loading.postValue(false)
            _capturesWithFishes.postValue(capturesWithFish)
        }
    }
}