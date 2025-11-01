package com.example.a3edhomework.horses.presentation.api

import kotlinx.serialization.Serializable

@Serializable
data class HorseApiModel(
    val id: String,
    val name: String,
    val owner: String,
    val majorWin: String?,
    val earnings: String,
    val imageUrl: String?
)