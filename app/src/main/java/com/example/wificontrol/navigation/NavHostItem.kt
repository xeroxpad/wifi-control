package com.example.wificontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.wificontrol.screens.authorization.AuthScreenObject
import com.example.wificontrol.screens.authorization.AuthorizationScreen
import com.example.wificontrol.screens.authorization.AuthorizationViewModel
import com.example.wificontrol.screens.devices.DeviceDetection
import com.example.wificontrol.screens.home.HomeScreen
import com.example.wificontrol.screens.profile.AccountData
import com.example.wificontrol.screens.profile.ProfileScreen
import com.example.wificontrol.screens.scannerresult.ScannerResultScreen
import com.example.wificontrol.screens.search.SearchScreen
import com.example.wificontrol.screens.statistics.StatisticsScreen
import okhttp3.internal.addHeaderLenient
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavHostItem(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Graph.AuthScreen.route,
        modifier = modifier
    ) {
        composable(Graph.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Graph.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Graph.Statistics.route) {
            StatisticsScreen()
        }
        composable(Graph.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Graph.DeviceDetection.route) {
            DeviceDetection(navController = navController)
        }
        composable(Graph.ScannerResult.route) {
            ScannerResultScreen(navController = navController)
        }
        composable(Graph.AuthScreen.route) {
            AuthorizationScreen(onLoginVk = {}, navController = navController)
        }
    }
}