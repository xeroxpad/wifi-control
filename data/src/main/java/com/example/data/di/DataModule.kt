package com.example.data.di

import com.example.data.api.RouterApiService
import com.example.data.api.VkApiService
import com.example.data.provideRetrofit
import com.example.data.provideSpeedApi
import com.example.data.repositories.IRouterRepositoryImpl
import com.example.data.repositories.ISpeedTestRepositoryImpl
import com.example.data.repositories.IWiFiRepositoryImpl
import com.example.domain.repositories.IRouterRepository
import com.example.domain.repositories.ISpeedTestRepository
import com.example.domain.repositories.IWiFiRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule =
    module {
        single<IWiFiRepository> { IWiFiRepositoryImpl(get()) }
        single<ISpeedTestRepository> { ISpeedTestRepositoryImpl(get()) }
        single { provideRetrofit() }
        single { provideSpeedApi(get()) }
        single {
            Retrofit.Builder()
                .baseUrl("http://192.168.8.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        single<RouterApiService> {
            get<Retrofit>().create(RouterApiService::class.java)
        }
        single<IRouterRepository> {
            IRouterRepositoryImpl(apiService = get(), username = "username", password = "password")
        }
        single<VkApiService> {
            get<Retrofit>().create(VkApiService::class.java)
        }
    }