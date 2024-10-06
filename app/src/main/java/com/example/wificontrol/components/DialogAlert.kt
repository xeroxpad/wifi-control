package com.example.wificontrol.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.wificontrol.screens.home.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DialogAlert(homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {
    AlertDialog(
        onDismissRequest = { homeScreenViewModel.toggleDialog() },
        confirmButton = {
            TextButton(onClick = { homeScreenViewModel.toggleDialog() }) {
                Text("OK")
            }
        },
        title = { Text("Подсказка") },
        text = { Text("Перейдите на вкладку - Search, чтобы добавить устройство") }
    )
}