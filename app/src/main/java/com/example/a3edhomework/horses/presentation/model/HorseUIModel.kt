package com.example.a3edhomework.horses.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class HorseUIModel(
    val id: String,
    val name: String,
    val owner: String,
    val majorWin: String?,
    val earnings: String,
    val imageUrl: String?
)
