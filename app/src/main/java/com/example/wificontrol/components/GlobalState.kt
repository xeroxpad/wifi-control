package com.example.wificontrol.components

import kotlinx.coroutines.flow.MutableStateFlow

object GlobalState {
    val isSessionExpired = MutableStateFlow(false)
}