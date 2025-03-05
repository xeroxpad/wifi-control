package com.example.wificontrol.screens.start

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wificontrol.navigation.BottomNavigationBar
import com.example.wificontrol.navigation.NavHostItem

@Composable
fun StartScreen(
    navController: NavHostController,
) {
    var bottomVisible by remember { mutableStateOf(true) }
    Scaffold(bottomBar = {
        if (bottomVisible) {
            BottomNavigationBar(
                navController = navController,
                state = bottomVisible,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }) { padding ->
        NavHostItem(
            navController = navController,
            modifier = Modifier.padding(padding)
        ) { isVisible ->
            bottomVisible = isVisible
        }
    }
}