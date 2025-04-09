package com.sunchen.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sunchen.sunnyweather.logic.Repository
import com.sunchen.sunnyweather.logic.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.switchMap

/**
 * @CreateTime 2025-04-09 16:34
 *
 * @Author sunchen
 *
 * @Description
 */

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeLiveData = searchLiveData.switchMap { place ->
        Repository.searchPlaces(place)
    }


    private val searchFlow = MutableStateFlow("")
    val places: StateFlow<Result<List<Place>>> = searchFlow.debounce(300).filter { it.isBlank() }.flatMapLatest { query ->
            Repository.searchPlacesFlow(query)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), Result.success(emptyList()))

    fun searchPlace(place: String) {
        // searchLiveData.value = place
        searchFlow.value = place
    }

}