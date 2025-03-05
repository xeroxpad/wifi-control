package com.example.wificontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wificontrol.screens.authorization.AuthorizationScreen
import com.example.wificontrol.screens.devices.DeviceDetection
import com.example.wificontrol.screens.home.HomeScreen
import com.example.wificontrol.screens.profile.ProfileScreen
import com.example.wificontrol.screens.scannerresult.ScannerResultScreen
import com.example.wificontrol.screens.search.SearchScreen
import com.example.wificontrol.screens.statistics.StatisticsScreen
import com.example.wificontrol.screens.support.ChatSupportScreen

@Composable
fun NavHostItem(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBottomVisibilityChange: (Boolean) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Graph.Home.route,
        modifier = modifier,
    ) {
        composable(Graph.Home.route) {
            onBottomVisibilityChange(true)
            HomeScreen(navController = navController)
        }
        composable(Graph.Search.route) {
            onBottomVisibilityChange(true)
            SearchScreen(navController = navController)
        }
        composable(Graph.Statistics.route) {
            onBottomVisibilityChange(true)
            StatisticsScreen()
        }
        composable(Graph.Profile.route) {
            onBottomVisibilityChange(true)
            ProfileScreen(navController = navController)
        }
        composable(Graph.DeviceDetection.route) {
            onBottomVisibilityChange(false)
            DeviceDetection(navController = navController)
        }
        composable(Graph.ScannerResult.route) {
            onBottomVisibilityChange(false)
            ScannerResultScreen(navController = navController)
        }
        composable(Graph.Auth.route) {
            onBottomVisibilityChange(false)
            AuthorizationScreen(onLoginVk = {}, navController = navController)
        }
        composable("${Graph.ChatSupport.route}/{chatId}") {backStackEntry ->
            onBottomVisibilityChange(false)
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatSupportScreen(navController = navController, chatId = chatId)
        }
    }
}