package com.example.a3edhomework.horses.presentation.domain.useCase

import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.repository.HorseRepository

class GetHorsesUseCase(private val repository: HorseRepository) {
    suspend operator fun invoke(): List<HorseUIModel> {
        return repository.getAllHorses()
    }
}
