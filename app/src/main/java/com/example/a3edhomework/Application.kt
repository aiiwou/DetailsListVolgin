package com.example.a3edhomework

import android.app.Application
import com.example.a3edhomework.horses.presentation.di.appModule.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HorseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HorseApplication)
            modules(appModule)
        }
    }
}