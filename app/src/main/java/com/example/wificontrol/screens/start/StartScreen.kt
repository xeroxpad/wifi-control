package com.example.wificontrol.screens.start

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wificontrol.navigation.BottomNavigationBar
import com.example.wificontrol.navigation.NavHostItem

@Composable
fun StartScreen(navController: NavHostController) {
    Scaffold(bottomBar = {
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
    }) { padding ->
        NavHostItem(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}