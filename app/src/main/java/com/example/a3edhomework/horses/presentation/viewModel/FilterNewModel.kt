package com.example.a3edhomework.horses.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3edhomework.horses.presentation.data.FilterDataStore
import com.example.a3edhomework.horses.presentation.domain.cache.BadgeCache
import com.example.a3edhomework.horses.presentation.model.HorseFilters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
class FilterViewModel(
    private val dataStore: FilterDataStore,
    private val badgeCache: BadgeCache
) : ViewModel() {

    private val _filtersState = MutableStateFlow(HorseFilters())
    val filtersState: StateFlow<HorseFilters> = _filtersState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.filtersFlow.collect { filters ->
                _filtersState.value = filters
                updateBadgeState(filters)
            }
        }
    }

    fun updateNameQuery(query: String) {
        _filtersState.value = _filtersState.value.copy(nameQuery = query)
    }

    fun updateMinEarnings(earnings: String) {
        _filtersState.value = _filtersState.value.copy(minEarnings = earnings)
    }

    fun updateOwner(owner: String) {
        _filtersState.value = _filtersState.value.copy(owner = owner)
    }

    fun saveFilters() {
        viewModelScope.launch {
            dataStore.saveFilters(_filtersState.value)
            updateBadgeState(_filtersState.value)
        }
    }

    fun clearFilters() {
        viewModelScope.launch {
            dataStore.clearFilters()
            _filtersState.value = HorseFilters()
            badgeCache.setHasActiveFilters(false)
        }
    }

    private fun updateBadgeState(filters: HorseFilters) {
        val hasFilters = filters.nameQuery.isNotEmpty() ||
                filters.minEarnings.isNotEmpty() ||
                filters.owner.isNotEmpty()
        badgeCache.setHasActiveFilters(hasFilters)
    }
}