package com.example.wificontrol.di

import com.example.wificontrol.screens.home.HomeScreenViewModel
import com.example.wificontrol.screens.profile.ProfileScreenViewModel
import com.example.wificontrol.screens.search.SearchScreenViewModel
import com.example.wificontrol.screens.statistics.StatisticsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule =
    module {
        viewModel { HomeScreenViewModel() }
        viewModel { ProfileScreenViewModel() }
        viewModel { SearchScreenViewModel() }
        viewModel { StatisticsScreenViewModel() }
    }