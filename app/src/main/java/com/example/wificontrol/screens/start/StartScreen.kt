package com.example.wificontrol.screens.start

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wificontrol.navigation.BottomNavigationBar
import com.example.wificontrol.navigation.Graph
import com.example.wificontrol.navigation.NavHostItem
import com.example.wificontrol.screens.authorization.AuthTokenStorage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun StartScreen(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    authTokenStorage: AuthTokenStorage,
) {
    val bottomBarState = rememberSaveable { mutableStateOf(true) }
    val initialLoggedIn = firebaseAuth.currentUser != null && authTokenStorage.getToken() != null
    val startDestination by remember { mutableStateOf(if (initialLoggedIn) Graph.Home.route else Graph.Auth.route) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val isLoggedIn = isUserLoggedIn(firebaseAuth, authTokenStorage)
            if (isLoggedIn && startDestination != Graph.Home.route) {
                navController.navigate(Graph.Home.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            } else if (!isLoggedIn && startDestination != Graph.Auth.route) {
                navController.navigate(Graph.Auth.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }

    Scaffold(bottomBar = {
        if (bottomBarState.value) {
            BottomNavigationBar(
                navController = navController,
                state = bottomBarState,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }) { padding ->
        NavHostItem(
            navController = navController,
            modifier = Modifier.padding(padding),
            bottomVisible = bottomBarState,
            startDestination = startDestination
        )
    }
}

private fun isUserLoggedIn(firebaseAuth: FirebaseAuth, storage: AuthTokenStorage): Boolean {
    val currentUser = firebaseAuth.currentUser
    val token = storage.getToken()
    return currentUser != null && token != null
}