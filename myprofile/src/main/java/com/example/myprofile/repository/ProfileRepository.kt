package com.example.myprofile.repository

import com.example.myprofile.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileFlow(): Flow<UserProfile>
    suspend fun saveProfile(profile: UserProfile)
    suspend fun clearProfile()
}