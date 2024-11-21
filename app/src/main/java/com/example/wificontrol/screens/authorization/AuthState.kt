package com.example.wificontrol.screens.authorization

sealed class AuthState {
    data object Initial: AuthState()
    data object Authorized: AuthState()
    data object NotAuthorized: AuthState()
}