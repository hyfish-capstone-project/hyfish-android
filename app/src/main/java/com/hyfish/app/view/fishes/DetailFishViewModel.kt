package com.hyfish.app.view.fishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hyfish.app.data.FishDetailRepository

class DetailFishViewModel(private val fishDetailRepository: FishDetailRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

//    private val _detailFishes = MutableLiveData<List<>>()
}