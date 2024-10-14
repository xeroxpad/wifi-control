package com.example.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import com.example.domain.usecases.GetWiFiNetworkUseCase
import com.example.domain.usecases.ScanLocalNetworkUseCase

val domainModule =
    module {
        factoryOf(::GetWiFiNetworkUseCase)
        factoryOf(::ScanLocalNetworkUseCase)
    }