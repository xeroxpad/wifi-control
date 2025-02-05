package com.example.wificontrol.screens.start

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wificontrol.navigation.BottomNavigationBar
import com.example.wificontrol.navigation.Graph
import com.example.wificontrol.navigation.NavHostItem
import com.example.wificontrol.screens.authorization.AuthorizationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(
    navController: NavHostController,
    authorizationViewModel: AuthorizationViewModel = koinViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val baseRoute = currentRoute?.substringBefore("/") ?: ""
    val bottomBarIsShow = rememberSaveable { (mutableStateOf(true)) }
    bottomBarIsShow.value = when (baseRoute) {
        Graph.Auth.route,
        Graph.ScannerResult.route,
        Graph.ChatSupport.route
        -> false
        else -> true
    }
    Scaffold(bottomBar = {
        when {
            bottomBarIsShow.value -> {
                BottomNavigationBar(
                    navController = navController,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }) { padding ->
        NavHostItem(
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}