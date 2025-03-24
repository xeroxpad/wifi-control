package com.example.wificontrol.screens.authorization

sealed class FirebaseAuthState {
    data object Initial : FirebaseAuthState()
    data object Authorized : FirebaseAuthState()
    data class Error(val message: String) : FirebaseAuthState()
}