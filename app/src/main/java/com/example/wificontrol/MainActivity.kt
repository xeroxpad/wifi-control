package com.example.wificontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.wificontrol.screens.authorization.AuthState
import com.example.wificontrol.screens.authorization.AuthorizationScreen
import com.example.wificontrol.screens.authorization.AuthorizationViewModel
import com.example.wificontrol.screens.home.HomeScreen
import com.example.wificontrol.screens.start.StartScreen
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val viewModel: AuthorizationViewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)
                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    viewModel.performAuthResult(it)
                }
                when (authState.value) {
                    is AuthState.Authorized -> {
                        StartScreen(navController = navController)
                    }
                    is AuthState.NotAuthorized -> {
                        AuthorizationScreen {
                            launcher.launch(listOf(VKScope.WALL))
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}