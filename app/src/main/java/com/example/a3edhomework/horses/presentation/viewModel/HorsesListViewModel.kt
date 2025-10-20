package com.example.a3edhomework.horses.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.a3edhomework.HorseDetails
import com.example.a3edhomework.horses.presentation.MockData
import com.example.a3edhomework.horses.presentation.domain.useCase.GetHorsesUseCase
import com.example.a3edhomework.horses.presentation.model.HorseListState
import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.repository.HorseRepository
import com.example.a3edhomework.navigation.Route
import com.example.a3edhomework.navigation.TopLevelBackStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HorsesListViewModel(private val getHorsesUseCase: GetHorsesUseCase) : ViewModel() {
    private val repository = HorseRepository()
    private val _viewState = MutableStateFlow<HorseListState>(HorseListState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        loadHorses()
    }

    fun loadHorses() {
        viewModelScope.launch {
            _viewState.value = HorseListState.Loading
            try {
                val horses = repository.getAllHorses()
                _viewState.value = HorseListState.Success(horses)
            } catch (e: Exception) {
                _viewState.value = HorseListState.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }
}