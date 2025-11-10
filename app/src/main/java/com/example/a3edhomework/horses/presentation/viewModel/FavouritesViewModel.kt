package com.example.a3edhomework.horses.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3edhomework.horses.presentation.model.HorseListState
import com.example.a3edhomework.horses.presentation.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _favoritesState = MutableStateFlow<HorseListState>(HorseListState.Loading)
    val favoritesState: StateFlow<HorseListState> = _favoritesState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            repository.getAllFavorites().collect { favorites ->
                if (favorites.isEmpty()) {
                    _favoritesState.value = HorseListState.Success(emptyList())
                } else {
                    _favoritesState.value = HorseListState.Success(favorites)
                }
            }
        }
    }

    suspend fun isFavorite(horseId: String): Boolean {
        return repository.isFavorite(horseId)
    }
}