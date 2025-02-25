package com.example.wificontrol.di

import com.example.wificontrol.screens.authorization.AuthorizationViewModel
import com.example.wificontrol.screens.home.HomeScreenViewModel
import com.example.wificontrol.screens.profile.ProfileScreenViewModel
import com.example.wificontrol.screens.scannerresult.ScannerResultScreenViewModel
import com.example.wificontrol.screens.search.SearchScreenViewModel
import com.example.wificontrol.screens.statistics.StatisticsScreenViewModel
import com.example.wificontrol.screens.support.ChatSupportScreen
import com.example.wificontrol.screens.support.ChatSupportViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel { HomeScreenViewModel() }
        viewModel { ProfileScreenViewModel(get()) }
        viewModel { SearchScreenViewModel() }
        viewModel { StatisticsScreenViewModel(get()) }
        viewModel { ScannerResultScreenViewModel(get()) }
        viewModel { AuthorizationViewModel(get()) }
        viewModel { ChatSupportViewModel() }
    }