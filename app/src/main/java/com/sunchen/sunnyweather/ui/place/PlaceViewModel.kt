package com.sunchen.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sunchen.sunnyweather.logic.Repository
import com.sunchen.sunnyweather.logic.model.Place
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

/**
 * @CreateTime 2025-04-09 16:34
 *
 * @Author sunchen
 *
 * @Description
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class PlaceViewModel : ViewModel() {


    private val queryFlow = MutableStateFlow("")
    val places: StateFlow<Result<List<Place>>> = queryFlow.debounce(300)
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            Repository.searchPlacesFlow(query)
        }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(3000), Result.success(emptyList())
        )

    val placeList = ArrayList<Place>()

    fun search(place: String) {
        // searchLiveData.value = place
        queryFlow.value = place
    }


    // private val _places = MutableStateFlow<Result<List<Place>>>(Result.success(emptyList()))
    // val places: StateFlow<Result<List<Place>>> = _places.asStateFlow()
    //
    // fun search(query: String) {
    //     viewModelScope.launch {
    //         Repository.searchPlacesFlow(query).collect { result ->
    //             _places.value = result
    //         }
    //     }
    // }

}