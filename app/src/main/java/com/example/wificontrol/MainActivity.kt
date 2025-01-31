package com.example.wificontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.wificontrol.screens.authorization.AuthState
import com.example.wificontrol.screens.authorization.AuthorizationScreen
import com.example.wificontrol.screens.authorization.AuthorizationViewModel
import com.example.wificontrol.screens.start.StartScreen
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val viewModel: AuthorizationViewModel = viewModel()
                val authStateVk by viewModel.authVkState.observeAsState(AuthState.Initial)
                val launcherVk = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    viewModel.performAuthResult(it)
                }

//                when (authStateVk) {
//                    is AuthState.Authorized -> {
//                        StartScreen(navController = navController)
//                    }
//
//                    is AuthState.NotAuthorized -> {
//                        AuthorizationScreen(
//                            onLoginVk = { launcherVk.launch(listOf(VKScope.WALL)) },
////                            navController = navController
//                        ) {}
//                    }
//
//                    else -> {}
//                }
                StartScreen(navController = navController)
            }
        }
    }
}