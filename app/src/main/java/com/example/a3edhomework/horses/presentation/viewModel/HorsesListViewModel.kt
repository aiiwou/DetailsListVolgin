package com.example.a3edhomework.horses.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.a3edhomework.HorseDetails
import com.example.a3edhomework.horses.presentation.MockData
import com.example.a3edhomework.horses.presentation.data.FilterDataStore
import com.example.a3edhomework.horses.presentation.domain.useCase.GetHorsesUseCase
import com.example.a3edhomework.horses.presentation.model.HorseFilters
import com.example.a3edhomework.horses.presentation.model.HorseListState
import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.repository.FavoritesRepository
import com.example.a3edhomework.horses.presentation.repository.HorseRepository
import com.example.a3edhomework.navigation.Route
import com.example.a3edhomework.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HorsesListViewModel(
    private val getHorsesUseCase: GetHorsesUseCase,
    private val filterDataStore: FilterDataStore,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<HorseListState>(HorseListState.Loading)
    val viewState: StateFlow<HorseListState> = _viewState.asStateFlow()

    private val _favoriteState = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoriteState: StateFlow<Map<String, Boolean>> = _favoriteState.asStateFlow()

    private var allHorses: List<HorseUIModel> = emptyList()
    private var currentFilters: HorseFilters = HorseFilters()

    init {
        loadFiltersAndHorses()
        loadAllFavorites()
    }

    private fun loadFiltersAndHorses() {
        viewModelScope.launch {
            filterDataStore.filtersFlow.collect { filters ->
                currentFilters = filters
                loadHorses()
            }
        }
    }

    private fun loadAllFavorites() {
        viewModelScope.launch {
            // Загружаем все избранные лошади для быстрого доступа
            allHorses.forEach { horse ->
                val isFavorite = favoritesRepository.isFavorite(horse.id)
                updateFavoriteState(horse.id, isFavorite)
            }
        }
    }

    fun loadHorses() {
        viewModelScope.launch {
            _viewState.value = HorseListState.Loading
            try {
                allHorses = getHorsesUseCase()
                applyFilters()
                loadAllFavorites() // Обновляем состояния избранного после загрузки лошадей
            } catch (e: Exception) {
                _viewState.value = HorseListState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    private fun applyFilters() {
        val filtered = allHorses.filter { horse ->
            val matchesName = horse.name.contains(currentFilters.nameQuery, ignoreCase = true) ||
                    currentFilters.nameQuery.isEmpty()

            val matchesEarnings = if (currentFilters.minEarnings.isNotEmpty()) {
                try {
                    val minEarnings = currentFilters.minEarnings.replace("[^\\d]".toRegex(), "").toLongOrNull() ?: 0L
                    val horseEarnings = horse.earnings.replace("[^\\d]".toRegex(), "").toLongOrNull() ?: 0L
                    horseEarnings >= minEarnings
                } catch (e: Exception) {
                    true
                }
            } else true

            val matchesOwner = horse.owner.contains(currentFilters.owner, ignoreCase = true) ||
                    currentFilters.owner.isEmpty()

            matchesName && matchesEarnings && matchesOwner
        }

        _viewState.value = HorseListState.Success(filtered)
    }

    // НЕБЛОКИРУЮЩИЙ метод для переключения избранного
    fun toggleFavorite(horse: HorseUIModel) {
        viewModelScope.launch {
            val isCurrentlyFavorite = favoritesRepository.isFavorite(horse.id)
            if (isCurrentlyFavorite) {
                favoritesRepository.removeFromFavorites(horse)
                updateFavoriteState(horse.id, false)
            } else {
                favoritesRepository.addToFavorites(horse)
                updateFavoriteState(horse.id, true)
            }
        }
    }

    fun checkFavorite(horseId: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val isFavorite = favoritesRepository.isFavorite(horseId)
            updateFavoriteState(horseId, isFavorite)
            onResult(isFavorite)
        }
    }

    fun isFavoriteCached(horseId: String): Boolean {
        return _favoriteState.value[horseId] ?: false
    }

    private fun updateFavoriteState(horseId: String, isFavorite: Boolean) {
        _favoriteState.value = _favoriteState.value + (horseId to isFavorite)
    }

    suspend fun isFavoriteSuspend(horseId: String): Boolean {
        return favoritesRepository.isFavorite(horseId)
    }

    suspend fun toggleFavoriteSuspend(horse: HorseUIModel) {
        val isCurrentlyFavorite = favoritesRepository.isFavorite(horse.id)
        if (isCurrentlyFavorite) {
            favoritesRepository.removeFromFavorites(horse)
        } else {
            favoritesRepository.addToFavorites(horse)
        }
        updateFavoriteState(horse.id, !isCurrentlyFavorite)
    }
}