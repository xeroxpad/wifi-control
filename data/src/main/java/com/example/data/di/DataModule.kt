package com.example.data.di

import com.example.data.repositories.IWiFiRepositoryImpl
import com.example.domain.repositories.IWiFiRepository
import org.koin.dsl.module

val dataModule =
    module {
        single<IWiFiRepository> { IWiFiRepositoryImpl(get()) }
    }