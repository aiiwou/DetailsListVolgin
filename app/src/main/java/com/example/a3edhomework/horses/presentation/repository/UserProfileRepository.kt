package com.example.a3edhomework.horses.presentation.repository

import com.example.a3edhomework.horses.presentation.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileFlow(): Flow<UserProfile>
    suspend fun saveProfile(profile: UserProfile)
    suspend fun clearProfile()
}