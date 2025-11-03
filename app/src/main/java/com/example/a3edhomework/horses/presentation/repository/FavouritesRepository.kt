package com.example.a3edhomework.horses.presentation.repository

import com.example.a3edhomework.horses.presentation.data.local.FavoriteHorseDao
import com.example.a3edhomework.horses.presentation.data.local.FavoriteHorseEntity
import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(private val dao: FavoriteHorseDao) {

    fun getAllFavorites(): Flow<List<HorseUIModel>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toHorseUIModel() }
        }
    }

    suspend fun addToFavorites(horse: HorseUIModel) {
        dao.addToFavorites(horse.toFavoriteEntity())
    }

    suspend fun removeFromFavorites(horse: HorseUIModel) {
        dao.removeFromFavoritesById(horse.id)
    }

    suspend fun isFavorite(horseId: String): Boolean {
        return dao.isFavorite(horseId)
    }

    private fun FavoriteHorseEntity.toHorseUIModel(): HorseUIModel {
        return HorseUIModel(
            id = this.id,
            name = this.name,
            owner = this.owner,
            majorWin = this.majorWin,
            earnings = this.earnings,
            imageUrl = this.imageUrl
        )
    }

    private fun HorseUIModel.toFavoriteEntity(): FavoriteHorseEntity {
        return FavoriteHorseEntity(
            id = this.id,
            name = this.name,
            owner = this.owner,
            majorWin = this.majorWin,
            earnings = this.earnings,
            imageUrl = this.imageUrl
        )
    }
}