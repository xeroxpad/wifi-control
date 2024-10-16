package com.example.wificontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wificontrol.screens.home.HomeScreen
import com.example.wificontrol.screens.profile.ProfileScreen
import com.example.wificontrol.screens.search.SearchScreen
import com.example.wificontrol.screens.statistics.StatisticsScreen

@Composable
fun NavHostItem(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Graph.Home.route,
        modifier = modifier
    ) {
        composable(Graph.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Graph.Search.route) {
            SearchScreen()
        }
        composable(Graph.Statistics.route) {
            StatisticsScreen()
        }
        composable(Graph.Profile.route) {
            ProfileScreen()
        }
    }
}