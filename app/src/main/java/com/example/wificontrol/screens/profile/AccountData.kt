package com.example.wificontrol.screens.profile

import kotlinx.serialization.Serializable


@Serializable
data class AccountData(
    val uid: String = "",
    val email: String = "",
)