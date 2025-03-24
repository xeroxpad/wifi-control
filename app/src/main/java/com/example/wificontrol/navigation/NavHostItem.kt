package com.example.wificontrol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.example.wificontrol.screens.support.AllChatScreen
import com.example.wificontrol.screens.support.ChatData
import com.example.wificontrol.screens.support.ChatSupportScreen

@Composable
fun NavHostItem(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomVisible: MutableState<Boolean>,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Graph.Home.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = true
            }
            HomeScreen(navController = navController)
        }
        composable(Graph.Search.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = true
            }
            SearchScreen(navController = navController)
        }
        composable(Graph.Statistics.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = true
            }
            StatisticsScreen()
        }
        composable(Graph.Profile.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = true
            }
            ProfileScreen(navController = navController)
        }
        composable(Graph.DeviceDetection.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = false
            }
            DeviceDetection(navController = navController)
        }
        composable(Graph.ScannerResult.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = false
            }
            ScannerResultScreen(navController = navController)
        }
        composable(Graph.Auth.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = false
            }
            AuthorizationScreen(onLoginVk = {}, navController = navController)
        }
        composable("${Graph.ChatSupport.route}/{chatId}") { backStackEntry ->
            LaunchedEffect(Unit) {
                bottomVisible.value = false
            }
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatSupportScreen(navController = navController, chatId = chatId,)
        }
        composable(Graph.AllChatsScreen.route) {
            LaunchedEffect(Unit) {
                bottomVisible.value = false
            }
            AllChatScreen(navController = navController)
        }
    }
}