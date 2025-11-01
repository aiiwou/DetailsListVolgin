package com.example.a3edhomework.horses.presentation.model

sealed interface HorseListState {
    object Loading : HorseListState
    data class Success(val horses: List<HorseUIModel>) : HorseListState
    data class Error(val message: String) : HorseListState
}