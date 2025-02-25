package com.example.wificontrol.screens.profile

data class AppState(
    val isSignedIn: Boolean = false,
    val userData: AccountData? = null,
    val signInError: String? = null,
    val srEmail: String = "",
)