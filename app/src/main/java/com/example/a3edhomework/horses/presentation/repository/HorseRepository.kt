package com.example.a3edhomework.horses.presentation.repository

import com.example.a3edhomework.horses.presentation.api.HorseApiModel
import com.example.a3edhomework.horses.presentation.api.HorseApiService
import com.example.a3edhomework.horses.presentation.api.RetrofitInstance
import com.example.a3edhomework.horses.presentation.model.HorseUIModel

class HorseRepository(
    private val api: HorseApiService
) {

    suspend fun getAllHorses(): List<HorseUIModel> {
        return api.getAllHorses()
    }

    suspend fun getHorseById(id: String): HorseUIModel {
        return api.getHorseById(id)
    }

    private fun HorseApiModel.toUIModel(): HorseUIModel {
        return HorseUIModel(
            id = this.id,
            name = this.name,
            owner = this.owner,
            majorWin = this.majorWin,
            earnings = this.earnings,
            imageUrl = this.imageUrl
        )
    }
}