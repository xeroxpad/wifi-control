package com.example.wificontrol.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.wificontrol.R
import com.example.wificontrol.screens.home.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DialogAlert(homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {
    AlertDialog(
        onDismissRequest = { homeScreenViewModel.toggleDialog() },
        confirmButton = {
            TextButton(onClick = { homeScreenViewModel.toggleDialog() }) {
                Text(stringResource(id = R.string.done))
            }
        },
        title = { Text(stringResource(id = R.string.hint)) },
        text = { Text(stringResource(id = R.string.go_to_search_tab)) }
    )
}