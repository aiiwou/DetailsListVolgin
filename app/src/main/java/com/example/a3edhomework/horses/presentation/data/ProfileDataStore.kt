package com.example.a3edhomework.horses.presentation.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.a3edhomework.horses.presentation.model.UserProfile
import com.example.a3edhomework.horses.presentation.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileDataStore(private val context: Context) : ProfileRepository {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "user_profile")
        private val KEY_FULL_NAME = stringPreferencesKey("profile_full_name")
        private val KEY_AVATAR_URI = stringPreferencesKey("profile_avatar_uri")
        private val KEY_RESUME_URL = stringPreferencesKey("profile_resume_url")
    }

    override fun getProfileFlow(): Flow<UserProfile> =
        context.dataStore.data.map { prefs ->
            UserProfile(
                fullName = prefs[KEY_FULL_NAME] ?: "",
                avatarUri = prefs[KEY_AVATAR_URI] ?: "",
                resumeUrl = prefs[KEY_RESUME_URL] ?: ""
            )
        }

    override suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_FULL_NAME] = profile.fullName
            prefs[KEY_AVATAR_URI] = profile.avatarUri
            prefs[KEY_RESUME_URL] = profile.resumeUrl
        }
    }

    override suspend fun clearProfile() {
        context.dataStore.edit { prefs -> prefs.clear() }
    }
}