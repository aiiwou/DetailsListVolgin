package com.example.a3edhomework.horses.presentation.api

import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import retrofit2.http.GET
import retrofit2.http.Path

interface HorseApiService {
    @GET("horses")
    suspend fun getAllHorses(): List<HorseUIModel>

    @GET("horses/{id}")
    suspend fun getHorseById(@Path("id") id: String): HorseUIModel
}