package com.example.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.example.domain.usecases.GetWiFiNetworkUseCase
import com.example.domain.usecases.GetSpeedTestUseCase
import com.example.domain.usecases.RebootRouterUseCase

val domainModule =
    module {
        factoryOf(::GetWiFiNetworkUseCase)
        factoryOf(::GetSpeedTestUseCase)
        factoryOf(::RebootRouterUseCase)
    }