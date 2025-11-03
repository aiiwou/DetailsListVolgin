package com.example.a3edhomework.horses.presentation.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.a3edhomework.horses.presentation.model.HorseFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilterDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "horse_filters")
        private val NAME_QUERY = stringPreferencesKey("name_query")
        private val MIN_EARNINGS = stringPreferencesKey("min_earnings")
        private val OWNER = stringPreferencesKey("owner")
    }

    val filtersFlow: Flow<HorseFilters> = context.dataStore.data.map { preferences ->
        HorseFilters(
            nameQuery = preferences[NAME_QUERY] ?: "",
            minEarnings = preferences[MIN_EARNINGS] ?: "",
            owner = preferences[OWNER] ?: ""
        )
    }

    suspend fun saveFilters(filters: HorseFilters) {
        context.dataStore.edit { preferences ->
            preferences[NAME_QUERY] = filters.nameQuery
            preferences[MIN_EARNINGS] = filters.minEarnings
            preferences[OWNER] = filters.owner
        }
    }

    suspend fun clearFilters() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}