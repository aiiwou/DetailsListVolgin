package com.example.a3edhomework.horses.presentation.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteHorseDao {
    @Query("SELECT * FROM favorite_horses ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteHorseEntity>>

    @Query("SELECT * FROM favorite_horses WHERE id = :horseId")
    suspend fun getFavoriteById(horseId: String): FavoriteHorseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(horse: FavoriteHorseEntity)

    @Delete
    suspend fun removeFromFavorites(horse: FavoriteHorseEntity)

    @Query("DELETE FROM favorite_horses WHERE id = :horseId")
    suspend fun removeFromFavoritesById(horseId: String)

    @Query("SELECT COUNT(*) FROM favorite_horses WHERE id = :horseId")
    suspend fun isFavorite(horseId: String): Boolean
}