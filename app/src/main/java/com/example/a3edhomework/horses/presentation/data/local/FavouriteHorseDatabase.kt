package com.example.a3edhomework.horses.presentation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteHorseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HorseDatabase : RoomDatabase() {
    abstract fun favoriteHorseDao(): FavoriteHorseDao

    companion object {
        @Volatile
        private var INSTANCE: HorseDatabase? = null

        fun getInstance(context: Context): HorseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HorseDatabase::class.java,
                    "horse_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}