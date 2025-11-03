package com.example.a3edhomework.horses.presentation.di.appModule

import android.content.Context
import com.example.a3edhomework.horses.presentation.api.HorseApiService
import com.example.a3edhomework.horses.presentation.api.RetrofitInstance
import com.example.a3edhomework.horses.presentation.data.FilterDataStore
import com.example.a3edhomework.horses.presentation.data.local.HorseDatabase
import com.example.a3edhomework.horses.presentation.domain.cache.BadgeCache
import com.example.a3edhomework.horses.presentation.domain.useCase.GetHorseByIdUseCase
import com.example.a3edhomework.horses.presentation.domain.useCase.GetHorsesUseCase
import com.example.a3edhomework.horses.presentation.repository.FavoritesRepository
import com.example.a3edhomework.horses.presentation.repository.HorseRepository
import com.example.a3edhomework.horses.presentation.viewModel.FavoritesViewModel
import com.example.a3edhomework.horses.presentation.viewModel.FilterViewModel
import com.example.a3edhomework.horses.presentation.viewModel.HorsesListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single<HorseApiService> {
        RetrofitInstance.api
    }

    single {
        HorseRepository(api = get())
    }

    single {
        GetHorsesUseCase(repository = get())
    }

    single {
        FilterDataStore(context = get())
    }

    single {
        HorseDatabase.getInstance(context = get())
    }
    single {
        get<HorseDatabase>().favoriteHorseDao()
    }

    single {
        FavoritesRepository(dao = get())
    }

    single {
        BadgeCache()
    }

    viewModel {
        FilterViewModel(
            dataStore = get(),
            badgeCache = get()
        )
    }

    viewModel {
        HorsesListViewModel(
            getHorsesUseCase = get(),
            filterDataStore = get(),
            favoritesRepository = get()
        )
    }

    viewModel {
        FavoritesViewModel(repository = get())
    }

}