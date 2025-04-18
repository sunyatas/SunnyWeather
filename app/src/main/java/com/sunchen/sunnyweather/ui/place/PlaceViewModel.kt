package com.sunchen.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sunchen.sunnyweather.logic.Repository
import com.sunchen.sunnyweather.logic.model.Place
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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

    // 供Adapter设置值
    val placeList = ArrayList<Place>()

    // 触发搜索
    fun search(place: String) {
        queryFlow.value = place
    }

    fun savePlace(place: Place) {
        viewModelScope.launch {
            Repository.savePlace(place)
        }
    }

    fun getPlace(): Flow<Place> {
        return Repository.getPlace()
    }

    fun isPlaceSaved() = Repository.isPlaceSaved()


}