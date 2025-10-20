package com.example.a3edhomework.horses.presentation.domain.useCase

import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.repository.HorseRepository

class GetHorseByIdUseCase(private val repository: HorseRepository) {
    suspend operator fun invoke(id: String): HorseUIModel {
        return repository.getHorseById(id)
    }
}