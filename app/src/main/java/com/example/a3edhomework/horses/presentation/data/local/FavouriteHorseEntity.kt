package com.example.a3edhomework.horses.presentation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_horses")
data class FavoriteHorseEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val owner: String,
    val majorWin: String?,
    val earnings: String,
    val imageUrl: String?,
    val addedAt: Long = System.currentTimeMillis()
)