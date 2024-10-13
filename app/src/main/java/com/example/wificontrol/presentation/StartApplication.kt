package com.example.wificontrol.presentation

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.wificontrol.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@StartApplication)
            modules(listOf(presentationModule, dataModule, domainModule))
        }
    }
}