package com.example.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myprofile.model.UserProfile
import com.example.myprofile.repository.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: ProfileRepository
) : ViewModel() {

    val profileState: StateFlow<UserProfile> =
        repo.getProfileFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, UserProfile())

    fun save(profile: UserProfile) {
        viewModelScope.launch {
            repo.saveProfile(profile)
        }
    }

    fun clear() {
        viewModelScope.launch { repo.clearProfile() }
    }
}